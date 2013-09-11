package com.edison.test.impl;

import java.util.Date;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;

public class RemoveEntityJob extends MyJob {

    private static final long serialVersionUID = 1386460325466037335L;
    private TestDAO td;
	
	public RemoveEntityJob(TestDAO td) {
		super();
		this.td = td;
	}

	@Override
	public void execute() {
		//System.out.println("Execute RemoveEntityJob");
		Date now = new Date();
		getDbManager().lock(td.getId());
		Date after = new Date();
		if(!now.equals(after))
		{
			System.out.println("I'm waiting! "+(after.getTime()-now.getTime()));
		}
		getDbManager().remove(td.getId());
		System.out.println("Removed cost "+(new Date().getTime()-now.getTime()));
	}

}
