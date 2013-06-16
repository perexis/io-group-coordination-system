/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.edu.agh.io.coordinator.utils.layersmenu;

public interface LayersMenuListener {

	void itemChecked(String item);
	void itemUnchecked(String item);
	void userChecked(String user);
	void userUnchecked(String user);
	void groupChecked(String group);
	void groupUnchecked(String group);
	void layerChecked(String layer);
	void layerUnchecked(String layer);
	
}

