package net.samystudio.beaver.ui.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import net.samystudio.beaver.BuildConfig
import net.samystudio.beaver.data.remote.CommandRequestState
import net.samystudio.beaver.di.scope.FragmentScope
import net.samystudio.beaver.ui.base.viewmodel.BaseFragmentViewModel
import net.samystudio.beaver.ui.base.viewmodel.DataPushViewModel
import net.samystudio.beaver.ui.common.viewmodel.RxCompletableCommand
import javax.inject.Inject

@FragmentScope
class AuthenticatorFragmentViewModel
@Inject
constructor(activityViewModel: AuthenticatorActivityViewModel) :
    BaseFragmentViewModel(activityViewModel), DataPushViewModel
{
    private val _dataPushObservable: RxCompletableCommand = RxCompletableCommand()
    override val dataPushObservable: LiveData<CommandRequestState> =
        _dataPushObservable.apply { disposables.add(this) }
    override val title: String?
        get() = "account"

    fun signIn(email: String, password: String)
    {
        _dataPushObservable.execute(
            userManager
                .signIn(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess({ handleSignResult(email, password) })
                .toCompletable()
        )
    }

    fun signUp(email: String, password: String)
    {
        _dataPushObservable.execute(
            userManager
                .signUp(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess({ handleSignResult(email, password) })
                .toCompletable()
        )
    }

    private fun handleSignResult(email: String, password: String)
    {
        val bundle = Bundle()
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, email)
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
                         BuildConfig.APPLICATION_ID)
        bundle.putString(AccountManager.KEY_PASSWORD, password)

        (activityViewModel as AuthenticatorActivityViewModel)
            .setAuthenticatorResult(bundle)

        // just to close activity
        activityViewModel.setResult(Activity.RESULT_OK, null)
    }
}