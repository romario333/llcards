package jobs;

import models.LocalUser;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

	@Override
	public void doJob() throws Exception {
		if (LocalUser.count() == 0) {
			Fixtures.load("initial-data.yml");
		}
	}
}
