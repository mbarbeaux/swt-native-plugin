/*
 * Copyright © 2021, Michael Barbeaux
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.mbarbeaux;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Mojo(name = "native", defaultPhase = LifecyclePhase.PACKAGE)
@Slf4j
public class SwtNativeMojo extends AbstractMojo {

    @Parameter(property = "mainClass", required = true)
    private String mainClass;

    @Parameter(required = true, defaultValue = "${project.build.directory}/swt-native")
    private File outputDirectory;

    public void execute() throws MojoExecutionException {
        log.info("MainClass = {}", mainClass);
        log.info("OutputDirectory = {}", outputDirectory);

        final Path outputPath = outputDirectory.toPath();
        if (Files.exists(outputPath) && !Files.isDirectory(outputPath)) {
            throw new MojoExecutionException("Cannot use " + outputPath.toString() + " as output directory, it is not a directory");
        }
        try {
            Files.createDirectory(outputPath);
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(outputPath.resolve("output.txt")))) {
                writer.println(mainClass);
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

}
