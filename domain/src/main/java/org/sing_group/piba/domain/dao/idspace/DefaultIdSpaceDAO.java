/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.piba.domain.dao.idspace;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.idspace.IdSpaceDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultIdSpaceDAO implements IdSpaceDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, IdSpace> dh;

  public DefaultIdSpaceDAO() {
    super();
  }

  public DefaultIdSpaceDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, IdSpace.class, this.em);
  }

  @Override
  public IdSpace get(String id) {
    return this.dh.get(id).orElseThrow(() -> new IllegalArgumentException("Unknown ID Space: " + id));
  }

  @Override
  public Stream<IdSpace> getIDSpaces() {
    return this.dh.list().stream();
  }

  @Override
  public IdSpace create(IdSpace idSpace) {
    return this.dh.persist(idSpace);
  }

  @Override
  public IdSpace edit(IdSpace idSpace) {
    return this.dh.update(idSpace);
  }

  @Override
  public void delete(IdSpace idSpace) {
    this.dh.remove(idSpace);
  }

}
