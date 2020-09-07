package com.kimbrelk.android.hamlogger.ui.booklist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.ui.mainactivity.MainActivity
import com.kimbrelk.android.hamlogger.ui.about.FragmentAbout
import com.kimbrelk.android.hamlogger.ui.bookedit.FragmentBookedit
import com.kimbrelk.android.hamlogger.ui.entrylist.FragmentEntrylist
import kotlinx.android.synthetic.main.recycler.view.*
import kotlinx.android.synthetic.main.recycler_add.view.*

class FragmentBooklist : Fragment {

    private val viewModel: ViewModelBooklist by activityViewModels()

    constructor() : super(R.layout.recycler_add) {
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = resources.getString(R.string.screen_books)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        val adapter = AdapterBooks(requireContext())
        adapter.callbackBookPressed = {
            (activity as MainActivity).push(FragmentEntrylist.create(it.id))
        }
        adapter.callbackBookLongPressed = {
            (activity as MainActivity).push(FragmentBookedit.create(it.id))
        }
        view.list.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        view.list.layoutManager = layoutManager
        val listDivider = DividerItemDecoration(context, layoutManager.orientation)
        viewModel.getBooks().observe(viewLifecycleOwner) { books ->
            adapter.updateItems(books)
            if (books.isNotEmpty()) {
                view.list.addItemDecoration(listDivider)
            } else {
                view.list.removeItemDecoration(listDivider)
            }
        }
        view.fab.setOnClickListener {
            val book = viewModel.createBook()
            (activity as MainActivity).push(FragmentEntrylist.create(book.id))
            (activity as MainActivity).push(FragmentBookedit.create(book.id, true))
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.books, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.action_about -> (activity as MainActivity).push(FragmentAbout())
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}