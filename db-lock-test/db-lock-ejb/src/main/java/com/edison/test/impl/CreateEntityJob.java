package com.edison.test.impl;

import java.util.Date;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;

public class CreateEntityJob extends MyJob {

    private static final long serialVersionUID = 3523180925434954266L;
    private TestDAO td;
    public CreateEntityJob(TestDAO td) {
        super();
        this.td = td;
    }


    @Override
    public void execute() {
        //System.out.println("Execute CreateEntityJob");
        td = getDbManager().create(td);
        td.getLock().lock();
        UpdateEntityJob updateJob = new UpdateEntityJob(td);
        getTimer().schedule(new Date(System.currentTimeMillis()+2000), updateJob);
        td.getLock().unlock();
    }

}
