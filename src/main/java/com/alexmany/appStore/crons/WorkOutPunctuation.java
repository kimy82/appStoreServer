package com.alexmany.appStore.crons;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alexmany.appStore.dao.UsersDao;
import com.alexmany.appStore.model.UserRole;
import com.alexmany.appStore.model.Users;

public class WorkOutPunctuation implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Crea elements necessaris per a la prova de l'app.
		System.out.println("Hello Quartz! Creem inserts BBDD");
		UsersDao usersDao = (UsersDao) context.getJobDetail().getJobDataMap()
				.get("usersDao");

		String entorn = (String) context.getJobDetail().getJobDataMap()
				.get("entorn");

		try {

			// creem els roles
			UserRole roleSA = new UserRole("ROLE_SUPER_ADMIN");
			UserRole roleA = new UserRole("ROLE_ADMIN");
			UserRole roleC = new UserRole("ROLE_CLIENT");
			usersDao.saveRole(roleSA);
			usersDao.saveRole(roleA);
			usersDao.saveRole(roleC);

			if (entorn.equals("local")) {
				// Creem els usuaris.
				Users userKim = new Users();
				userKim.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
				userKim.setUsername("joaquim.orra@gmail.com");
				userKim.setUserRole(roleSA);
				userKim.setEmail("email000");
				usersDao.save(userKim);
				
				Users userAlex = new Users();
				userAlex.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
				userAlex.setUsername("adp.alex@gmail.com");
				userAlex.setUserRole(roleSA);
				userAlex.setEmail("email00");
				usersDao.save(userAlex);

				Users userAl = new Users();
				userAl.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
				userAl.setUsername("joaquim.orra2@gmail.com");
				userAl.setUserRole(roleC);
				userAl.setEmail("email0");
				usersDao.save(userAl);

				Users userAl1menu = new Users();
				userAl1menu
						.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
				userAl1menu.setUsername("joaquim.orra3@gmail.com");
				userAl1menu.setUserRole(roleC);
				userAl1menu.setEmail("email1");
				usersDao.save(userAl1menu);
				
				Users userAl2menu = new Users();
				userAl2menu
						.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
				userAl2menu.setUsername("joaqñim.òrrá@gmail.es");
				userAl2menu.setEmail("email2");
				userAl2menu.setUserRole(roleC);
				usersDao.save(userAl2menu);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
