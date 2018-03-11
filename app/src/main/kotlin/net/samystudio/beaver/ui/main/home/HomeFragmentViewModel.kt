package net.samystudio.beaver.ui.main.home

import android.arch.lifecycle.LiveData
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.data.remote.DataRequestState
import net.samystudio.beaver.data.remote.api.HomeApiInterface
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataFetchViewModel
import net.samystudio.beaver.ui.common.viewmodel.RxSingleLiveData
import net.samystudio.beaver.ui.main.MainActivityViewModel
import javax.inject.Inject

@FragmentScope
class HomeFragmentViewModel
@Inject
constructor(activityViewModel: MainActivityViewModel,
            homeApiManager: HomeApiInterface) :
    BaseFragmentViewModel(activityViewModel), DataFetchViewModel<Home>
{
    private val _dataFetchObservable: RxSingleLiveData<Home> =
        RxSingleLiveData(homeApiManager.home())
    override val dataFetchObservable: LiveData<DataRequestState<Home>> =
        _dataFetchObservable.apply { disposables.add(this) }
    override val title: String?
        get() = "home"

    override fun refreshData()
    {
        _dataFetchObservable.refresh()
    }
}