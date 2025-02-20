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

package app.ss.lessons.data.repository.quarterly

import app.ss.lessons.data.api.SSQuarterliesApi
import app.ss.storage.db.dao.LanguagesDao
import com.cryart.sabbathschool.core.extensions.connectivity.ConnectivityHelper
import com.cryart.sabbathschool.test.coroutines.TestDispatcherProvider
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class LanguagesDataSourceTest {

    private val mockLanguagesDao: LanguagesDao = mockk()
    private val mockQuarterliesApi: SSQuarterliesApi = mockk()
    private val mockConnectivityHelper: ConnectivityHelper = mockk()

    private val dispatcherProvider = TestDispatcherProvider()

    private lateinit var dataSource: LanguagesDataSource

    @Before
    fun setup() {
        every { mockConnectivityHelper.isConnected() }.returns(true)

        dataSource = LanguagesDataSource(
            dispatcherProvider = dispatcherProvider,
            connectivityHelper = mockConnectivityHelper,
            languagesDao = mockLanguagesDao,
            quarterliesApi = mockQuarterliesApi
        )
    }

    @Test
    fun `should format native language name correctly`() {
        dataSource.getNativeLanguageName("en", "English") shouldBeEqualTo "English"
        dataSource.getNativeLanguageName("es", "Spanish") shouldBeEqualTo "Español"
        dataSource.getNativeLanguageName("fr", "French") shouldBeEqualTo "Français"
    }
}
