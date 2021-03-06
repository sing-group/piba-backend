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
import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.io.FilenameUtils.getExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;

import org.sing_group.piba.service.spi.storage.FileStorage;

@Default
public class DefaultFileStorage implements FileStorage {

  private static final String PATH_CONFIG_NAME = "java:global/piba/defaultfilestorage/path";

  @Resource(name = PATH_CONFIG_NAME)
  private String path;

  public DefaultFileStorage() {}

  public DefaultFileStorage(String path) {
    this.path = path;
  }

  @Override
  public void store(String id, String format, InputStream data) {
    final Path file = getFileForId(id, format);

    if (exists(file)) {
      throw new IllegalArgumentException("File already exists: " + file);
    }
    try {
      copy(data, file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path getFileForId(String id, String format) {
    return getBasePath().resolve(id + "." + format);
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
  public InputStream retrieve(String id, String format) {
    final Path file = getFileForId(id, format);
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
    walkOverFilesWithId(id)
      .forEach(
        path -> {
          try {
            Files.delete(path);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      );
  }

  @Override
  public Set<String> getAllIds() {
    Set<String> ids = new HashSet<String>();
    walkOverFiles().forEach(path -> {
      File file = path.toFile();
      ids.add(file.getName().split("\\.")[0]);
    });
    return ids;
  }

  @Override
  public long getLength(String id, String format) {
    return getFileForId(id, format).toFile().length();
  }

  @Override
  public Set<String> getFormatsForId(String id) {
    return walkOverFilesWithId(id)
      .map(path -> getExtension(path.toFile().getName()))
      .collect(toSet());
  }

  private Stream<Path> walkOverFilesWithId(String id) {
    return walkOverFiles().filter(path -> path.toFile().getName().startsWith(id));
  }

  private Stream<Path> walkOverFiles() {
    try {
      return walk(getBasePath()).filter(path -> !path.equals(getBasePath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
