package com.edison.test.impl;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;

public class ReupdateEntityJob extends MyJob {

    private static final long serialVersionUID = -6580329948508058678L;
    private TestDAO td;
	
	
	public ReupdateEntityJob(TestDAO td) {
		super();
		this.td = td;
	}

	@Override
	public void execute() {
	    //System.out.println("Execute ReupdateEntityJob");
	    td.getLock().lock();
		//getDbManager().lock(td.getId());
		RemoveEntityJob removeJob = new RemoveEntityJob(td);
		/*getTimer().schedule(0l, removeJob);
		try {
			Thread.sleep(150l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		System.out.println("Execute Reupdate " + td.getId());
		td.setAttribute("Reupdate");
		getDbManager().update(td);
		td.getLock().unlock();
	}

}
