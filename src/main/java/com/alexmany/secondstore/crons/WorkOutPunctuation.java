package com.alexmany.secondstore.crons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alexmany.secondstore.dao.ChatsDao;
import com.alexmany.secondstore.dao.UsersDao;
import com.alexmany.secondstore.model.Chats;
import com.alexmany.secondstore.model.Comments;
import com.alexmany.secondstore.model.UserRole;
import com.alexmany.secondstore.model.Users;

public class WorkOutPunctuation implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Crea elements necessaris per a la prova de l'app.
		System.out.println("Hello Quartz! Creem inserts BBDD");
		UsersDao usersDao = (UsersDao) context.getJobDetail().getJobDataMap()
				.get("usersDao");
		
		ChatsDao chatsDao = (ChatsDao) context.getJobDetail().getJobDataMap()
				.get("chatsDao");

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
				
				//INsert chats for testing app
				List<Users> usersTOchat = new ArrayList<Users>();
				usersTOchat.add(userAl1menu);
				usersTOchat.add(userAl2menu);
				
				List<Comments> comments = new ArrayList<Comments>();
				Comments comment1 = new Comments();
				Comments comment2 = new Comments();
				Comments comment3 = new Comments();
				Chats chat = new Chats();
				comment1.setChat(chat);
				comment2.setChat(chat);
				comment3.setChat(chat);
				comment1.setComment("Hola estic interesat amb el surf");
				comment2.setComment("Doncs hi ha un lloc que es diu newquay");
				comment3.setComment("Uau alla vaig");
				comment1.setDate(new Date());
				comment2.setDate(new Date());
				comment3.setDate(new Date());
				comments.add(comment1);
				comments.add(comment2);
				comments.add(comment3);
				chat.setComments(comments);
				chat.setUsers(usersTOchat);
				chatsDao.save(chat);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
