/*-
 * #%L
 * Service
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

package org.sing_group.piba.service.storage;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.isDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;

import org.sing_group.piba.service.spi.storage.FileStorage;

@Default
public class DefaultFileStorage implements FileStorage {

  private static final String PATH_CONFIG_NAME = "java:global/piba/defaultfilestorage/path";
  private static final String[] EXTENSIONS = {
    ".mp4", ".ogg"
  };

  @Resource(name = PATH_CONFIG_NAME)
  private String path;

  public DefaultFileStorage() {}

  public DefaultFileStorage(String path) {
    this.path = path;
  }

  @Override
  public void store(String id, InputStream data) {
    final Path file = getFileForId(id);

    if (exists(file)) {
      throw new IllegalArgumentException("File already exists: " + file);
    }
    try {
      copy(data, file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path getFileForId(String id) {
    return getBasePath().resolve(id);
  }

  private Path getBasePath() {
    final Path p = Paths.get(this.path);

    if (!exists(p) || !isDirectory(p)) {
      throw new IllegalArgumentException(
        String.format(
          "path for file storage is invalid: %s. Check that you have configured the global resource name '%s' correctly",
          this.path, PATH_CONFIG_NAME
        )
      );
    }
    return p;
  }

  @Override
  public InputStream retrieve(String id) {
    final Path file = getFileForId(id);
    if (!exists(file)) {
      throw new IllegalArgumentException("Cannot find file for id: " + id);
    }

    try {
      return new FileInputStream(file.toFile());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(String id) {
    final Path file = getFileForId(id);
    for (String extension : EXTENSIONS) {
      new File(file + extension).delete();
    }
  }

  @Override
  public Set<String> getAllIds() {
    Set<String> ids = new HashSet<String>();
    String[] files = new File(getBasePath().toString()).list();
    for (String file : files) {
      ids.add(file.split("\\.")[0]);
    }
    return ids;
  }

  @Override
  public long getLength(String id) {
    return getFileForId(id).toFile().length();
  }
}
