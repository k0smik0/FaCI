package net.iubris.facri.persisters.specialized;

import javax.inject.Inject;

import net.iubris.facri.model.parser.users.Ego;
import net.iubris.facri.persister.helpers.EgoFacriBerkeleyDBAutoCommittableHelper;
import net.iubris.facri.persister.persisters.base.FacriPersister;

public class EgoPersister extends FacriPersister<Ego> {

	@Inject
	public EgoPersister(EgoFacriBerkeleyDBAutoCommittableHelper egoFacriBerkeleyDBAutoCommittableHelper/*, HelpersHolder helpersHolder*/) {
		super(egoFacriBerkeleyDBAutoCommittableHelper, Ego.class/*, helpersHolder*/);
	}
}
