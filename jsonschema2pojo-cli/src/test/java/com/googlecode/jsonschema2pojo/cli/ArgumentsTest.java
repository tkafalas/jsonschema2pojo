/**
 * Copyright © 2010-2011 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jsonschema2pojo.cli;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArgumentsTest {

    private final static PrintStream SYSTEM_OUT = System.out;
    private final ByteArrayOutputStream systemOutCapture = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(systemOutCapture));
    }

    @After
    public void tearDown() {
        System.setOut(SYSTEM_OUT);
    }

    @Test
    public void parseRecognisesValidArguments() {
        ArgsForTest args = (ArgsForTest) new ArgsForTest().parse(new String[] {"--source", "/home/source", "--target", "/home/target", "--package", "mypackage"});

        assertThat(args.getStatus().hasCaptured(), is(false));
        assertThat(args.getSource().getAbsolutePath(), is("/home/source"));
        assertThat(args.getTargetDirectory().getAbsolutePath(), is("/home/target"));
        assertThat(args.getTargetPackage(), is("mypackage"));
    }

    @Test
    public void packageIsOptional() {
        ArgsForTest args = (ArgsForTest) new ArgsForTest().parse(new String[] {"-s", "/home/source", "-t", "/home/target"});

        assertThat(args.getStatus().hasCaptured(), is(false));
        assertThat(args.getSource().getAbsolutePath(), is("/home/source"));
        assertThat(args.getTargetDirectory().getAbsolutePath(), is("/home/target"));
        assertThat(args.getTargetPackage(), is(nullValue()));
    }

    @Test
    public void missingArgsCausesHelp() throws IOException {
        ArgsForTest args = (ArgsForTest) new ArgsForTest().parse(new String[] {});

        assertThat(args.getStatus().hasCaptured(), is(true));
        assertThat(args.getStatus().getValue(), is(1));
        assertThat(new String(systemOutCapture.toByteArray(), "UTF-8"), is(containsString("Usage: jsonschema2pojo")));
    }

    @Test
    public void requestingHelpCausesHelp() throws IOException {
        ArgsForTest args = (ArgsForTest) new ArgsForTest().parse(new String[] {"--help"});

        assertThat(args.getStatus().hasCaptured(), is(true));
        assertThat(new String(systemOutCapture.toByteArray(), "UTF-8"), is(containsString("Usage: jsonschema2pojo")));
    }

    private static class ArgsForTest extends Arguments {
        protected Capture<Integer> status = new Capture<Integer>();

        @Override
        protected void exit(int status) {
            this.status.setValue(status);
        }

        public Capture<Integer> getStatus() {
            return status;
        }

    }

}
