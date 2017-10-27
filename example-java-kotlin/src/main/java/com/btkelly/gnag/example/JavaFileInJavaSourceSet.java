/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.btkelly.gnag.example;

/**
 * Created by bobbake4 on 2/7/17.
 */
public class JavaFileInJavaSourceSet {

    public static void main(String[] args) {

        String test = "";
        String testCompare = "Fail";

        if (test == testCompare) {
            System.out.print("Total Fail");
        }

        try {
            System.out.print("Empty");
        } catch (Exception e) {

        } finally {

        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
