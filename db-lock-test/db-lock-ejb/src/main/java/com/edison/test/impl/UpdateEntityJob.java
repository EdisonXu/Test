package com.edison.test.impl;

import java.util.Date;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;

public class UpdateEntityJob extends MyJob {

    private static final long serialVersionUID = -4634426316070405136L;
    private TestDAO td;
	
	
	public UpdateEntityJob(TestDAO td) {
		super();
		this.td = td;
	}

	@Override
	public void execute() {
	    //System.out.println("Execute UpdateEntityJob");
		td.getLock().lock();
		//getDbManager().lock(td.getId());
		ReupdateEntityJob job = new ReupdateEntityJob(td);
		getTimer().schedule(new Date(), job);
		/*try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		System.out.println("Execute Update " + td.getId());
		td.setAttribute("Update");
		getDbManager().update(td);
		td.getLock().unlock();
		
	}

}
