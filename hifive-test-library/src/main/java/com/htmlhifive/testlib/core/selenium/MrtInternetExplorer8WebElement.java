/*
 * Copyright (C) 2015 NS Solutions Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.htmlhifive.testlib.core.selenium;

/**
 * Internet Explorer 8で利用する{@link org.openqa.selenium.WebElement}
 */
class MrtInternetExplorer8WebElement extends MrtInternetExplorerWebElement {

	@Override
	public WebElementRect getRect() {
		// IE7と8はbody以外座標を-2する
		WebElementRect rect = super.getRect();
		if ("body".equals(getTagName())) {
			return rect;
		}

		return new WebElementRect(rect.getLeft() - 2d, rect.getTop() - 2d, rect.getWidth(), rect.getHeight());
	}
}