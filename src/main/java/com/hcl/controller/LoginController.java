package com.hcl.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

import com.hcl.model.UserPojo;

@WebServlet("/login")
public class LoginController extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		String name = req.getParameter("uname");
		String pwd = req.getParameter("pwd");

		UserPojo u1 = new UserPojo();
		u1.setPassword(pwd);
		u1.setUsername(name);
		req.setAttribute("abc", u1);

		boolean status = false;

		EntityManagerFactory emf = null;
		EntityManager entityManager = null;
		EntityTransaction transaction = null;

		try {
			emf = Persistence.createEntityManagerFactory("jbd-pu");
			entityManager = emf.createEntityManager();
			transaction = entityManager.getTransaction();

			transaction.begin();
			UserPojo user1 = new UserPojo();
			user1.setUsername("Hadi");
			user1.setPassword("12345");
			entityManager.persist(user1);
			transaction.commit();

			transaction.begin();
			UserPojo user2 = new UserPojo();
			user2.setUsername("Joseph");
			user2.setPassword("54321");
			entityManager.persist(user2);
			transaction.commit();

			Query readAll = entityManager.createQuery("select s from UserPojo s");

			List<UserPojo> resultListAll = readAll.getResultList();
			System.out.println("num of sudents:" + resultListAll.size());
			for (UserPojo next : resultListAll) {
				if (next.getUsername().equals(u1.getUsername()) && next.getPassword().equals(u1.getPassword())) {
					status = true;
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
			if (emf != null) {
				emf.close();
			}
		}
		if (status) {
			RequestDispatcher rd = req.getRequestDispatcher("success.jsp");
			rd.forward(req, res);
		} else {
			RequestDispatcher rd = req.getRequestDispatcher("failure.jsp");
			rd.forward(req, res);
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

}
