/*
 * Copyright (c) 2022. Adventech <info@adventech.io>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cryart.sabbathschool.lessons.ui.lessons

import androidx.compose.runtime.Immutable
import app.ss.models.PublishingInfo
import app.ss.models.SSQuarterlyInfo

@Immutable
sealed class PublishingInfoState {
    data class Success(val publishingInfo: PublishingInfo) : PublishingInfoState()
    object Loading : PublishingInfoState()
    object Error : PublishingInfoState()
}

@Immutable
sealed class QuarterlyInfoState {
    data class Success(val quarterlyInfo: SSQuarterlyInfo) : QuarterlyInfoState()
    object Loading : QuarterlyInfoState()
    object Error : QuarterlyInfoState()
}

@Immutable
data class LessonsScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val quarterlyInfo: QuarterlyInfoState = QuarterlyInfoState.Loading,
    val publishingInfo: PublishingInfoState = PublishingInfoState.Loading
)

internal val LessonsScreenState.quarterlyTitle: String get() =
    (quarterlyInfo as? QuarterlyInfoState.Success)?.quarterlyInfo?.quarterly?.title ?: ""
