/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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

package org.sing_group.piba.rest.mapper;

import static org.apache.commons.lang.exception.ExceptionUtils.getRootCause;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class IllegalArgumentExceptionMapper
  implements ExceptionMapper<IllegalArgumentException> {
  private final static Logger LOG = LoggerFactory.getLogger(IllegalArgumentException.class);

  @Override
  public Response toResponse(IllegalArgumentException e) {
    LOG.error("Exception catched", e);

    return Response.status(Response.Status.BAD_REQUEST)
      .entity((getRootCause(e) != null ? getRootCause(e) : e).getMessage())
      .type(MediaType.TEXT_PLAIN)
      .build();
  }
}
