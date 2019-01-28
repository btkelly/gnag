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
package com.btkelly.gnag.example

/**
 * Created by bobbake4 on 2/7/17.
 */
class KotlinFileInKotlinSourceSet {

    override fun toString(): String = super.toString()

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            val test = ""
            val testCompare = "Fail"

            if (test === testCompare) {
                print("Total Fail")
            }

            try {
                print("Empty")
            } catch (e: Exception) {

            } finally {

            }
        }
    }

}
