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
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;

import org.sing_group.piba.service.spi.storage.FileStorage;

@Default
public class DefaultFileStorage implements FileStorage {

  private static final String PATH_CONFIG_NAME = "java:global/piba/defaultfilestorage/path";
  private static final String ORIGINAL_PATH_CONFIG_NAME = "java:global/piba/defaultfilestorage/original";

  private static final Logger LOGGER = Logger.getLogger(DefaultFileStorage.class.getName());

  @Resource(name = PATH_CONFIG_NAME)
  private String path;
  @Resource(name = ORIGINAL_PATH_CONFIG_NAME)
  private String originalPath;

  public DefaultFileStorage() {}

  public DefaultFileStorage(String path, String originalPath) {
    this.path = path;
    this.originalPath = originalPath;
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

  @Override
  public void storeOriginal(String id, String name, Supplier<InputStream> data) {
    final Path file;

    if ((file = getOriginalFileForVideo(id, name)) != null) {
      try (InputStream input = data.get()) {
        copy(input, file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Path getFileForId(String id, String format) {
    return getBasePath().resolve(id + "." + format);
  }

  private Path getOriginalFileForVideo(String id, String name) {
    final Path p = getOriginalBasePath();

    // The file name comes from the client. Be extra careful
    Path sanitizedFilename;
    try {
      // Convert to absolute path to get rid of path traversal sequences
      sanitizedFilename = Paths.get(id + "_" + name).toAbsolutePath().getFileName();
      if (sanitizedFilename == null) {
        throw new InvalidPathException(name, "Sanitization failed");
      }
    } catch (InvalidPathException e) {
      // Handle the name containing characters invalid for a path (on Unix systems, NUL)
      // or sanitization going so wrong that we don't have a file name path component
      sanitizedFilename = Paths.get(id + "_unknown.bin");
    }

    return p != null ? p.resolve(sanitizedFilename) : null;
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

  private Path getOriginalBasePath() {
    final Path p = !this.originalPath.equals("DISABLED") ? Paths.get(this.originalPath) : null;

    if (p != null && (!exists(p) || !isDirectory(p))) {
      throw new IllegalArgumentException(
        String.format(
          "path for original file storage is invalid: %s. Check that you have configured the global resource name '%s' correctly",
          this.originalPath, ORIGINAL_PATH_CONFIG_NAME
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
    Stream.concat(
      walkOverFilesWithId(getBasePath(), id),
      walkOverFilesWithId(getOriginalBasePath(), id)
    )
    .forEach(
      path -> {
        try {
          LOGGER.info("Deleting " + path);
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
    walkOverFiles(getBasePath()).forEach(path -> {
      final File file = path.toFile();
      final String filename = file.getName().split("\\.")[0];

      boolean isUUID = true;
      try {
        UUID.fromString(filename);
      } catch (IllegalArgumentException e) {
        isUUID = false;
      }

      if (isUUID && file.isFile()) {
        ids.add(filename);
      }
    });
    return ids;
  }

  @Override
  public long getLength(String id, String format) {
    return getFileForId(id, format).toFile().length();
  }

  @Override
  public Set<String> getFormatsForId(String id) {
    return walkOverFilesWithId(getBasePath(), id)
      .map(path -> getExtension(path.toFile().getName()))
      .collect(toSet());
  }

  private Stream<Path> walkOverFilesWithId(Path basePath, String id) {
    return walkOverFiles(basePath).filter(path -> path.toFile().getName().startsWith(id));
  }

  private Stream<Path> walkOverFiles(Path basePath) {
    try {
      final Stream<Path> fileStream = basePath != null ? walk(basePath) : Stream.empty();
      return fileStream.filter(path -> !path.equals(basePath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
