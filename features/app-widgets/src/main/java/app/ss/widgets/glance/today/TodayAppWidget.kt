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

package app.ss.widgets.glance.today

import android.content.Context
import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import app.ss.widgets.R
import app.ss.widgets.WidgetDataProvider
import app.ss.widgets.glance.BaseGlanceAppWidget
import app.ss.widgets.glance.extensions.clickable
import app.ss.widgets.glance.extensions.modifyAppWidgetBackground
import app.ss.widgets.glance.extensions.stringResource
import app.ss.widgets.glance.extensions.toAction
import app.ss.widgets.glance.theme.SsGlanceTheme
import app.ss.widgets.glance.theme.todayBody
import app.ss.widgets.glance.theme.todayTitle
import app.ss.widgets.model.TodayWidgetModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ss.misc.DateHelper
import app.ss.translations.R as TranslationsR

private typealias Data = TodayWidgetModel

internal class TodayAppWidget @AssistedInject constructor(
    private val dataProvider: WidgetDataProvider,
    @Assisted context: Context
) : BaseGlanceAppWidget<Data?>(context = context) {

    override val sizeMode = SizeMode.Responsive(
        setOf(smallMode, mediumMode, largeMode)
    )

    override suspend fun loadData(): Data? = dataProvider.getTodayModel()

    @Composable
    override fun Content(data: Data?) {
        val isSmallMode = LocalSize.current == smallMode

        SsGlanceTheme {
            Box(
                modifier = GlanceModifier
                    .modifyAppWidgetBackground()
                    .clickable(uri = data?.uri)
            ) {
                WidgetAppLogo()

                TodayInfo(
                    spec = TodayInfoSpec(
                        model = data,
                        titleMaxLines = if (isSmallMode) 2 else 3,
                        bodyMaxLines = if (isSmallMode) 1 else 2,
                        showReadButton = !isSmallMode
                    )
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context): TodayAppWidget
    }

    companion object {
        private val smallMode = DpSize(180.dp, 55.dp)
        private val mediumMode = DpSize(180.dp, 110.dp)
        private val largeMode = DpSize(300.dp, 110.dp)
    }
}

@Composable
private fun WidgetAppLogo(
    modifier: GlanceModifier = GlanceModifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = GlanceModifier.defaultWeight())

        Image(
            provider = ImageProvider(R.drawable.ic_widget_logo),
            contentDescription = stringResource(TranslationsR.string.ss_app_name),
            modifier = GlanceModifier
                .size(AppLogoSize)
                .padding(
                    top = (-40).dp,
                    end = (-35).dp
                )
        )
    }
}

private val AppLogoSize = 70.dp

@Composable
internal fun TodayInfo(
    spec: TodayInfoSpec,
    modifier: GlanceModifier = GlanceModifier
) {
    val model = spec.model ?: TodayWidgetModel(
        stringResource(R.string.ss_widget_error_label),
        DateHelper.today(),
        "",
        Uri.EMPTY
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            ),
        verticalAlignment = Alignment.Bottom
    ) {
        Spacer(modifier = GlanceModifier.defaultWeight())

        Text(
            text = model.date,
            style = todayBody(spec.textColor),
            maxLines = spec.bodyMaxLines
        )

        Spacer(modifier = GlanceModifier.height(6.dp))

        Text(
            text = model.title,
            style = todayTitle(spec.textColor),
            maxLines = spec.titleMaxLines
        )

        Spacer(modifier = GlanceModifier.height(12.dp))

        if (spec.showReadButton) {
            Button(
                text = stringResource(TranslationsR.string.ss_lessons_read).uppercase(),
                style = todayTitle(
                    MaterialTheme.colorScheme.onPrimary
                ).copy(fontSize = 14.sp),
                colors = ButtonColors(
                    backgroundColor = ColorProvider(MaterialTheme.colorScheme.primary),
                    contentColor = ColorProvider(MaterialTheme.colorScheme.onPrimary),
                ),
                maxLines = 1,
                onClick = model.uri.toAction(),
                modifier = GlanceModifier
                    .cornerRadius(20.dp)
                    .padding(horizontal = 32.dp)
                    .height(32.dp)
            )
        }
    }
}
