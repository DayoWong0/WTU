package grade.xyj.com.util.extend

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope

//toast
fun Fragment.toastSuccess(s: String) = activity?.toastSuccess(s)

fun Fragment.toastError(s: String) = activity?.toastError(s)
fun Fragment.toastInfo(s: String) = activity?.toastInfo(s)

fun Fragment.showProgressDialog(msg: String) = activity?.showProgressDialog(msg)


inline fun <reified T : ViewModel> Fragment.getViewModel(): T = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.getActivityViewModel(): T = ViewModelProviders.of(requireActivity()).get(T::class.java)

fun Fragment.getColor(id: Int) = requireActivity().getColorByCompat(id)

fun Fragment.delayed(delayMillis: Long, action: suspend CoroutineScope.() -> Unit) = requireActivity().delayed(delayMillis, action)
