package com.alexmany.secondstore.crons;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alexmany.secondstore.dao.AnuncisDao;
import com.alexmany.secondstore.model.Anuncis;
import com.alexmany.secondstore.utils.Constants;

public class WorkOutAnuncis implements Job {
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// Crea elements necessaris per a la prova de l'app.
		System.out.println("Hello Quartz! Creem inserts BBDD");
		AnuncisDao anuncisDao = (AnuncisDao) context.getJobDetail().getJobDataMap()
				.get("anuncisDao");


		try {
			List<Anuncis> anuncisList = anuncisDao.getAll();
			Calendar checkDateOneWeek = Calendar.getInstance();
			checkDateOneWeek.set(Calendar.DAY_OF_YEAR, checkDateOneWeek.get(Calendar.DAY_OF_YEAR)-7);
			Date dateOneWeek = checkDateOneWeek.getTime();
			
			Calendar checkDateOneMonth = Calendar.getInstance();
			checkDateOneMonth.set(Calendar.DAY_OF_YEAR, checkDateOneMonth.get(Calendar.DAY_OF_YEAR)-30);
			Date dateOneMonth = checkDateOneWeek.getTime();
			
			if(anuncisList!=null && !anuncisList.isEmpty()){
				for(Anuncis anunci : anuncisList){
					if(anunci.getDataCreacio().before(dateOneWeek)){
						anunci.setEstat(Constants.ESTAT_ANUNCI_NORMAL);
					}else if (anunci.getDataCreacio().before(dateOneMonth)){
						anunci.setEstat(Constants.ESTAT_ANUNCI_VELL);
					}else{
						anunci.setEstat(Constants.ESTAT_ANUNCI_NEW);
					}
					anuncisDao.update(anunci);
				}
			}
			// creem els roles
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
