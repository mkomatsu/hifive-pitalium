/*
 * Copyright (C) 2015-2017 NS Solutions Corporation
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

package com.htmlhifive.pitalium.it.screenshot.exclude;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.htmlhifive.pitalium.core.model.ScreenshotArgument;
import com.htmlhifive.pitalium.core.model.TargetResult;
import com.htmlhifive.pitalium.image.model.RectangleArea;
import com.htmlhifive.pitalium.it.screenshot.PtlItScreenshotTestBase;

/**
 * 複数の要素を除外するテスト
 */
public class ExcludeMultiElementsTest extends PtlItScreenshotTestBase {

	/**
	 * 単体要素のスクリーンショットを撮影し、そこに含まれる複数の要素を単体のセレクタを使用して除外する。
	 *
	 * @ptl.expect 除外領域が正しく保存されていること。
	 */
	@Test
	public void singleTarget() throws Exception {
		openBasicColorPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByTagName("body")
				.addExcludeByClassName("color-column").build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), hasSize(3));

		for (int i = 0; i < 3; i++) {
			Rect rect = getRectById("colorColumn" + i).toExcludeRect();
			RectangleArea area = result.getExcludes().get(i).getRectangle();
			assertThat(area, is(rect.toRectangleArea()));
		}
	}

	/**
	 * 単体要素のスクリーンショットを撮影し、そこに含まれる複数の要素を複数のセレクタを使用して除外する。
	 *
	 * @ptl.expect 除外領域が正しく保存されていること。
	 */
	@Test
	public void multiTargets() throws Exception {
		openBasicColorPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByTagName("body")
				.addExcludeById("colorColumn0").addExcludeById("colorColumn1").addExcludeById("colorColumn2").build();
		assertionView.assertView(arg);

		// Check
		TargetResult result = loadTargetResults("s").get(0);
		assertThat(result.getExcludes(), hasSize(3));

		for (int i = 0; i < 3; i++) {
			Rect rect = getRectById("colorColumn" + i).toExcludeRect();
			RectangleArea area = result.getExcludes().get(i).getRectangle();
			assertThat(area, is(rect.toRectangleArea()));
		}
	}

	/**
	 * 複数のスクリーンショットを撮影し、それぞれから要素を除外する。
	 *
	 * @ptl.expect 除外領域が正しく保存されていること。
	 */
	@Test
	public void nestedTargets() throws Exception {
		openBasicColorPage();

		ScreenshotArgument arg = ScreenshotArgument.builder("s").addNewTargetByTagName("body")
				.addExcludeById("colorColumn0").addNewTargetById("container").addExcludeById("colorColumn0").build();
		assertionView.assertView(arg);

		// Check
		List<TargetResult> results = loadTargetResults("s");
		assertThat(results, hasSize(2));

		Rect containerRect = getRectById("container");
		Rect columnRect = getRectById("colorColumn0");

		RectangleArea containerArea = containerRect.toTargetRect().toRectangleArea();
		RectangleArea columnArea = columnRect.toExcludeRect().toRectangleArea();

		// Body
		TargetResult bodyResult = results.get(0);
		assertThat(bodyResult.getExcludes(), hasSize(1));
		assertThat(bodyResult.getExcludes().get(0).getRectangle(), is(columnArea));

		// Container
		TargetResult containerResult = results.get(1);
		assertThat(containerResult.getExcludes(), hasSize(1));
		assertThat(containerResult.getExcludes().get(0).getRectangle(),
				is(new RectangleArea(columnArea.getX() - containerArea.getX(), columnArea.getY() - containerArea.getY(),
						columnArea.getWidth(), columnArea.getHeight())));
	}

}
