package com.kimbrelk.android.hamlogger.ui.bookedit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.ui.mainactivity.MainActivity
import com.kimbrelk.android.hamlogger.utils.Utils
import kotlinx.android.synthetic.main.frag_edit_book.view.*

class FragmentBookedit : Fragment {

    private val viewModel: ViewModelBookedit by activityViewModels()

    companion object {
        private const val KEY_BOOK_ID: String = "book_id"
        private const val KEY_IS_NEW: String = "is_new"

        fun create(bookId: String, isNew: Boolean = false) : FragmentBookedit {
            val fragment = FragmentBookedit()
            val args = Bundle()
            args.putString(KEY_BOOK_ID, bookId)
            args.putBoolean(KEY_IS_NEW, isNew)
            fragment.arguments = args
            return fragment
        }
    }

    constructor() : super(R.layout.frag_edit_book) {
        setHasOptionsMenu(true)
    }

    private fun getBookId() : String = requireArguments().getString(KEY_BOOK_ID)!!

    private fun isNew() : Boolean = requireArguments().getBoolean(KEY_IS_NEW)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        viewModel.getBook(getBookId()).observe(viewLifecycleOwner) { book ->
            if (book != null) {
                view.txt_title.setText(book.title)
                view.txt_call.setText(book.callsign)
                view.swt_same_call.isChecked = book.hasMultipleOps
                view.swt_show_section_contest_fieldday.isChecked = book.showContestFieldday
                view.swt_show_section_location_grid.isChecked = book.showLocGrid
                view.txt_location_grid_tx.setText(book.gridSquare)
                view.swt_show_section_power.isChecked = book.showPower
                view.swt_show_section_signal.isChecked = book.showSignal
                view.swt_show_section_comments.isChecked = book.showComments
                view.swt_show_utc.isChecked = book.showTimeAsUTC
            }
        }
        view.fab.setOnClickListener {
            (requireActivity() as MainActivity).pop()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                viewModel.deleteBook(getBookId())
                (requireActivity() as MainActivity).pop()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        var titleRes = R.string.screen_book_edit
        if (isNew()) {
            titleRes = R.string.screen_book_new
        }
        requireActivity().title = resources.getString(titleRes)
    }

    override fun onStop() {
        super.onStop()
        saveBook()
    }

    private fun saveBook() {
        if (view != null) {
            val book = Book(
                id = getBookId(),
                title = Utils.emptyStringToNull(requireView().txt_title.text.toString()),
                callsign = Utils.emptyStringToNull(requireView().txt_call.text.toString()),
                hasMultipleOps = requireView().swt_same_call.isChecked,
                showContestFieldday = requireView().swt_show_section_contest_fieldday.isChecked,
                showLocGrid = requireView().swt_show_section_location_grid.isChecked,
                gridSquare = Utils.emptyStringToNull(requireView().txt_location_grid_tx.text.toString()),
                showPower = requireView().swt_show_section_power.isChecked,
                showSignal = requireView().swt_show_section_signal.isChecked,
                showComments = requireView().swt_show_section_comments.isChecked,
                showTimeAsUTC = requireView().swt_show_utc.isChecked
            )
            viewModel.saveBook(book)
        }
    }

}