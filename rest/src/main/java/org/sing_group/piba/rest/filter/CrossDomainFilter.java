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


package org.sing_group.piba.rest.filter;

import static java.util.Arrays.asList;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class CrossDomainFilter implements ContainerResponseFilter {
  private final CrossDomainBuilder builder;
  
  public CrossDomainFilter() {
    this.builder = new CrossDomainBuilder();
  }
  
  @Override
  public void filter(ContainerRequestContext containerRequest, ContainerResponseContext containerResponse) throws IOException {
    if (containerRequest.getMethod().equals("OPTIONS")) {
      final MultivaluedMap<String, Object> responseHeaders = containerResponse.getHeaders();

      final CrossDomain annotation = CrossDomainInterceptor.class.getAnnotation(CrossDomain.class);
      
      this.builder.buildCorsPreFlightHeaders(
        new AnnotationCrossDomainConfiguration(annotation),
        (key, value) -> responseHeaders.put(key, asList(value)),
        containerRequest::getHeaderString
      );
    }
  }
}
