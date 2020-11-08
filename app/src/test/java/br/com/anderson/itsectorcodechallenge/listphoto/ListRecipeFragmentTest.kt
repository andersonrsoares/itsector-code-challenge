package br.com.anderson.itsectorcodechallenge.listphoto

import android.app.Application
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.anderson.itsectorcodechallenge.R
import br.com.anderson.itsectorcodechallenge.RecyclerViewMatcher
import br.com.anderson.itsectorcodechallenge.ViewModelUtil
import br.com.anderson.itsectorcodechallenge.model.Photo
import br.com.anderson.itsectorcodechallenge.ui.listphoto.ListPhotoFragment
import br.com.anderson.itsectorcodechallenge.ui.listphoto.ListPhotoFragmentDirections
import br.com.anderson.itsectorcodechallenge.ui.listphoto.ListPhotoViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class, qualifiers = "w360dp-h880dp-xhdpi")
class ListRecipeFragmentTest {

    lateinit var testviewModel: ListPhotoViewModel

    lateinit var factory: FragmentFactory

    val mockNavController = Mockito.mock(NavController::class.java)

    @Before
    fun setup() {
        testviewModel = Mockito.mock(ListPhotoViewModel::class.java)
        factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return ListPhotoFragment()
                    .apply {
                        this.factory = ViewModelUtil.createFor(testviewModel)
                    }
            }
        }
    }

    @Test fun `test list photo ui recycleview list click item`() {
        val liveDataListRecipes = MutableLiveData<List<Photo>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val clean = MutableLiveData<Boolean>()
        BDDMockito.given(testviewModel.dataPhoto).willReturn(liveDataListRecipes)
        BDDMockito.given(testviewModel.loading).willReturn(loading)
        BDDMockito.given(testviewModel.message).willReturn(message)
        BDDMockito.given(testviewModel.retry).willReturn(retry)
        BDDMockito.given(testviewModel.clean).willReturn(clean)

        liveDataListRecipes.value = arrayListOf(Photo(smallUrl = "url1", id = "id1", downloadUrl = "url1"))
        val scenario = launchFragmentInContainer<ListPhotoFragment>(themeResId = R.style.Theme_Itsectorcodechallenge, factory = factory)

        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }

        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(listMatcher().atPosition(0)).perform(ViewActions.click())

        Mockito.verify(mockNavController).navigate(ListPhotoFragmentDirections.actionListFotoFragmentToFotoFragment("url1"))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test fun `test list photo ui error and retry`() {
        val liveDataListRecipes = MutableLiveData<List<Photo>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val clean = MutableLiveData<Boolean>()
        BDDMockito.given(testviewModel.dataPhoto).willReturn(liveDataListRecipes)
        BDDMockito.given(testviewModel.loading).willReturn(loading)
        BDDMockito.given(testviewModel.message).willReturn(message)
        BDDMockito.given(testviewModel.retry).willReturn(retry)
        BDDMockito.given(testviewModel.clean).willReturn(clean)

        retry.value = "error and retry"
        val scenario = launchFragmentInContainer<ListPhotoFragment>(themeResId = R.style.Theme_Itsectorcodechallenge, factory = factory)

        onView(ViewMatchers.withText("error and retry")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.retrybutton)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(ViewMatchers.withId(R.id.retrybutton)).perform(ViewActions.click())
        liveDataListRecipes.value = arrayListOf(Photo(smallUrl = "url1", id = "id1", downloadUrl = "url1"))
        onView(listMatcher().atPosition(0)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test fun `test list photo ui error message`() {
        val liveDataListRecipes = MutableLiveData<List<Photo>>()
        val loading = MutableLiveData<Boolean>()
        val message = MutableLiveData<String>()
        val retry = MutableLiveData<String>()
        val clean = MutableLiveData<Boolean>()
        BDDMockito.given(testviewModel.dataPhoto).willReturn(liveDataListRecipes)
        BDDMockito.given(testviewModel.loading).willReturn(loading)
        BDDMockito.given(testviewModel.message).willReturn(message)
        BDDMockito.given(testviewModel.retry).willReturn(retry)
        BDDMockito.given(testviewModel.clean).willReturn(clean)

        message.value = "error"
        val scenario = launchFragmentInContainer<ListPhotoFragment>(themeResId = R.style.Theme_Itsectorcodechallenge, factory = factory)

        onView(ViewMatchers.withText("error")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycleview)
    }
}
