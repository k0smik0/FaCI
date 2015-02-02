/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PersisterException.java) is part of facri.
 * 
 *     PersisterException.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PersisterException.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console.actions.graph.utils.cache.persister.exception;

public class PersisterException extends Exception {

	private static final long serialVersionUID = 5568812581306093192L;

	public PersisterException() {
	}

	public PersisterException(String message) {
		super(message);
	}

	public PersisterException(Throwable cause) {
		super(cause);
	}

	public PersisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
