package net.iubris.facri.persisters;

import javax.inject.Inject;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.persisters.base.FacriPersister;
import net.iubris.facri.persisters.helpers.EgoFacriBerkeleyDBAutoCommittableHelper;

public class EgoPersister extends FacriPersister<Ego> {

	@Inject
	public EgoPersister(EgoFacriBerkeleyDBAutoCommittableHelper egoFacriBerkeleyDBAutoCommittableHelper/*, HelpersHolder helpersHolder*/) {
		super(egoFacriBerkeleyDBAutoCommittableHelper, Ego.class/*, helpersHolder*/);
	}
}
