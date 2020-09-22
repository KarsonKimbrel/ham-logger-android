package com.kimbrelk.android.hamlogger.ui.entrylist

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.ui.bookedit.FragmentBookedit
import com.kimbrelk.android.hamlogger.ui.entryedit.FragmentEntryedit
import com.kimbrelk.android.hamlogger.ui.mainactivity.MainActivity
import com.kimbrelk.android.hamlogger.utils.Constants
import com.kimbrelk.android.hamlogger.utils.SortMode
import kotlinx.android.synthetic.main.recycler.view.*
import kotlinx.android.synthetic.main.recycler_add.view.*

class FragmentEntrylist : Fragment {

    private val viewModel: ViewModelEntrylist by activityViewModels()

    companion object {

        private const val KEY_BOOK_ID = "book_id"

        fun create(bookId: String) : FragmentEntrylist {
            val fragment = FragmentEntrylist()
            val args = Bundle()
            args.putString(KEY_BOOK_ID, bookId)
            fragment.arguments = args
            return fragment
        }
    }

    constructor() : super(R.layout.recycler_add) {
        setHasOptionsMenu(true)
    }

    private fun getBookId() : String = requireArguments().getString(KEY_BOOK_ID)!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.bookId = getBookId()
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        val adapter = AdapterEntries(requireContext())
        adapter.callbackEntryPressed = {
            (activity as MainActivity).push(FragmentEntryedit.create(it.bookId, it.id))
        }
        view.list.adapter = adapter
        val layoutManager = LinearLayoutManager(context)
        view.list.layoutManager = layoutManager
        val listDivider = DividerItemDecoration(context, layoutManager.orientation)
        viewModel.getEntries().observe(viewLifecycleOwner) { entries ->
            adapter.updateItems(entries)
            if (entries.isNotEmpty()) {
                view.list.addItemDecoration(listDivider)
            } else {
                view.list.removeItemDecoration(listDivider)
            }
        }
        viewModel.getBook().observe(viewLifecycleOwner) { book ->
            if (book != null) {
                adapter.updateBook(book)
                requireActivity().title = book.title ?: resources.getString(R.string.hint_untitled_book)
                view.fab.setOnClickListener {
                    val entry = viewModel.createEntry(book)
                    (activity as MainActivity).push(FragmentEntryedit.create(entry.bookId, entry.id, true))
                }
            }
            else {
                (requireActivity() as MainActivity).pop()
            }
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.entries, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = searchViewItem.actionView as SearchView
        if (viewModel.searchQuery.isNotEmpty()) {
            searchViewItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery, true)
        }
        searchView.setIconifiedByDefault(true)
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String) : Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String) : Boolean {
                    viewModel.searchQuery = newText
                    return true
                }
            }
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.action_export_adi -> viewModel.adiExport()
            R.id.action_import_adi -> {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.Request.PERMISSION_STORAGE_EXTERNAL_READ)
                } else {
                    startImportDialog()
                }
            }
            R.id.action_prefs_logbook -> (activity as MainActivity).push(FragmentBookedit.create(getBookId()))
            R.id.action_sort_by_call -> viewModel.sortMode = SortMode.Callsign
            R.id.action_sort_by_date -> viewModel.sortMode = SortMode.Date
            R.id.action_sort_by_freq -> viewModel.sortMode = SortMode.Frequency
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun startImportDialog() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            val intentChooser = Intent.createChooser(intent, "Select an ADI file to Upload")
            requireActivity().startActivityForResult(intentChooser, Constants.Request.IMPORT_ADI)
        } catch (e: ActivityNotFoundException) {
            Log.w(this::class.simpleName, "Please install a File Manager.")
            Toast.makeText(context, "Please install a File Manager.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.Request.IMPORT_ADI -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data!!
                    viewModel.adiImport(requireContext(), uri)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}