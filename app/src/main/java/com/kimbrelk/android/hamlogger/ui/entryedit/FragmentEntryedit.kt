package com.kimbrelk.android.hamlogger.ui.entryedit

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.data.legacydatabase.model.ARRL_SECTION
import com.kimbrelk.android.hamlogger.data.legacydatabase.model.Mode
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.data.model.ModeSubModePair
import com.kimbrelk.android.hamlogger.ui.mainactivity.MainActivity
import com.kimbrelk.android.hamlogger.utils.Utils
import com.kimbrelk.android.hamlogger.view.ClickToSelectEditText
import kotlinx.android.synthetic.main.frag_edit_entry.view.*
import kotlinx.android.synthetic.main.section_callsigns_2.view.txt_call_rx
import kotlinx.android.synthetic.main.section_callsigns_2.view.txt_call_tx
import kotlinx.android.synthetic.main.section_callsigns_3.view.*
import kotlinx.android.synthetic.main.section_comments.view.*
import kotlinx.android.synthetic.main.section_contest_fieldday.view.*
import kotlinx.android.synthetic.main.section_loc_grid.view.*
import kotlinx.android.synthetic.main.section_power.view.*
import kotlinx.android.synthetic.main.section_signal.view.*
import java.util.*


class FragmentEntryedit : Fragment {

    private val viewModel: ViewModelEntryedit by activityViewModels()

    private var entryTimestamp: Long = 0L
    private lateinit var entryModeSubModePair: ModeSubModePair

    companion object {

        private const val KEY_BOOK_ID = "book_id"
        private const val KEY_ENTRY_ID = "entry_id"
        private const val KEY_IS_NEW = "is_new"

        fun create(bookId: String, entryId: String, isNew: Boolean = false) : FragmentEntryedit {
            val fragment = FragmentEntryedit()
            val args = Bundle()
            args.putString(KEY_BOOK_ID, bookId)
            args.putString(KEY_ENTRY_ID, entryId)
            args.putBoolean(KEY_IS_NEW, isNew)
            fragment.arguments = args
            return fragment
        }
    }

    constructor() : super(R.layout.frag_edit_entry) {
        setHasOptionsMenu(true)
    }

    private fun getBookId() : String = requireArguments().getString(KEY_BOOK_ID)!!

    private fun getEntryId() : String = requireArguments().getString(KEY_ENTRY_ID)!!

    private fun isNew() : Boolean = requireArguments().getBoolean(KEY_IS_NEW)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)!!
        viewModel.getBook(getBookId()).observe(viewLifecycleOwner) { book ->
            if (book != null) {
                if (book.hasMultipleOps) view.stub_section_callsigns_3.inflate()
                else view.stub_section_callsigns_2.inflate()
                if (book.showComments) view.stub_section_comments.inflate()
                if (book.showContestFieldday) view.stub_section_contest_fieldday.inflate()
                if (book.showLocGrid) view.stub_section_loc_grid.inflate()
                if (book.showPower) view.stub_section_power.inflate()
                if (book.showSignal) view.stub_section_signal.inflate()
                view.layout_time.hint = getString(R.string.hint_time_x,
                    book.getTimeZone().getDisplayName(
                        book.getTimeZone().inDaylightTime(Date(System.currentTimeMillis())),
                        TimeZone.SHORT
                    )
                )
                viewModel.getEntry(getBookId(), getEntryId()).observe(viewLifecycleOwner) { entry ->
                    if (entry != null) {
                        entryTimestamp = entry.timestamp

                        // Time and Date
                        view.txt_time.setText(Utils.formatTimeReadable(entryTimestamp, book.getTimeZone()))
                        view.txt_date.setText(Utils.formatDateReadable(entryTimestamp, book.getTimeZone()))
                        // Frequency
                        view.txt_freq.setText(entry.frequency.toString())
                        // Callsigns
                        view.txt_call_tx.setText(entry.callTx)
                        view.txt_call_rx.setText(entry.callRx)
                        view.txt_call_op?.setText(entry.callOp)
                        // Comments
                        view.txt_comments_rx?.setText(entry.commentsRx)
                        view.txt_comments_tx?.setText(entry.commentsTx)
                        // Contest Field Day
                        view.txt_contest_class?.setText(entry.contestClass)
                        view.txt_contest_class_transmitters?.setText(entry.contestClassTransmitters?.toString())
                        view.txt_contest_section?.setText(entry.contestSection)
                        // Grid Squares
                        view.txt_loc_grid_rx?.setText(entry.locGridRx)
                        view.txt_loc_grid_tx?.setText(entry.locGridTx)
                        // Power Report
                        view.txt_power_rx?.setText(entry.powerReportRx)
                        view.txt_power_tx?.setText(entry.powerReportTx)
                        // Signal Report
                        view.txt_signal_rx?.setText(entry.signalReportRx)
                        view.txt_signal_tx?.setText(entry.signalReportTx)
                        // Time Input
                        view.txt_time.setOnClickListener {
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = entryTimestamp
                            calendar.timeZone = book.getTimeZone()
                            val dialog = TimePickerDialog(
                                requireContext(),
                                TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                                    val calendar1 = Calendar.getInstance()
                                    calendar1.timeInMillis = entryTimestamp
                                    calendar1.timeZone = book.getTimeZone()
                                    calendar1[Calendar.HOUR_OF_DAY] = hourOfDay
                                    calendar1[Calendar.MINUTE] = minute
                                    entryTimestamp = calendar1.timeInMillis
                                    view.txt_time.setText(Utils.formatTimeReadable(entryTimestamp, book.getTimeZone()))
                                },
                                calendar[Calendar.HOUR_OF_DAY],
                                calendar[Calendar.MINUTE],
                                DateFormat.is24HourFormat(context)
                            )
                            dialog.show()
                        }
                        // Date Input
                        view.txt_date.setOnClickListener {
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = entryTimestamp
                            calendar.timeZone = book.getTimeZone()
                            val dialog = DatePickerDialog(
                                requireContext(),
                                OnDateSetListener { _: DatePicker?, year: Int, month: Int, day: Int ->
                                    val calendar1 = Calendar.getInstance()
                                    calendar1.timeInMillis = entryTimestamp
                                    calendar1.timeZone = book.getTimeZone()
                                    calendar1[Calendar.YEAR] = year
                                    calendar1[Calendar.MONTH] = month
                                    calendar1[Calendar.DAY_OF_MONTH] = day
                                    entryTimestamp = calendar1.timeInMillis
                                    view.txt_date.setText(Utils.formatDateReadable(entryTimestamp, book.getTimeZone()))
                                },
                                calendar[Calendar.YEAR],
                                calendar[Calendar.MONTH],
                                calendar[Calendar.DAY_OF_MONTH]
                            )
                            dialog.show()
                        }
                        // Mode Input
                        val txtMode = view.txt_mode as ClickToSelectEditText<ModeSubModePair>
                        txtMode.items = Mode.getModeSubModePairs()
                        txtMode.setOnItemSelectedListener { modeSubModePair: ModeSubModePair, _: Int ->
                            entryModeSubModePair = modeSubModePair
                            txtMode.setText(modeSubModePair.label)
                        }
                        entryModeSubModePair = entry.getModeSubModePair()
                        txtMode.setText(entryModeSubModePair.label)
                        // Contest Class Input
                        view.txt_contest_class?.setItems(R.array.arrl_classes)
                        view.txt_contest_class?.setOnItemSelectedListener { _class, selectedIndex -> view.txt_contest_class.setText(_class.label) }
                        // Contest Section Input
                        val txtContestSection = view.txt_contest_section as ClickToSelectEditText<ARRL_SECTION>?
                        txtContestSection?.setItems(ARRL_SECTION.values())
                        txtContestSection?.setText(ARRL_SECTION.values()[0].label)
                        txtContestSection?.setOnItemSelectedListener { section, selectedIndex -> view.txt_contest_section.setText(section.label) }
                    }
                }
            }
            else {
                (requireActivity() as MainActivity).pop()
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
                viewModel.deleteEntry(getBookId(), getEntryId())
                (requireActivity() as MainActivity).pop()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        var titleRes = R.string.screen_entry_edit
        if (isNew()) {
            titleRes = R.string.screen_entry_new
        }
        requireActivity().title = resources.getString(titleRes)
    }

    override fun onStop() {
        super.onStop()
        saveEntry()
    }

    private fun saveEntry() {
        if (view != null) {
            val v = requireView()
            val entry = Entry(
                id = getEntryId(),
                bookId = getBookId(),
                callOp = Utils.emptyStringToNull(v.txt_call_op?.text.toString()),
                callRx = Utils.emptyStringToNull(v.txt_call_rx?.text.toString()),
                callTx = Utils.emptyStringToNull(v.txt_call_tx?.text.toString()),
                commentsRx = Utils.emptyStringToNull(v.txt_comments_rx?.text.toString()),
                commentsTx = Utils.emptyStringToNull(v.txt_comments_tx?.text.toString()),
                contestClass = Utils.emptyStringToNull(v.txt_contest_class?.text.toString()),
                contestClassTransmitters = v.txt_contest_class_transmitters?.text.toString().toIntOrNull(),
                contestSection = Utils.emptyStringToNull(v.txt_contest_section?.text.toString()),
                frequency = Utils.stringToDouble(v.txt_freq?.text.toString()),
                locGridRx = Utils.emptyStringToNull(v.txt_loc_grid_rx?.text.toString()),
                locGridTx = Utils.emptyStringToNull(v.txt_loc_grid_tx?.text.toString()),
                mode = entryModeSubModePair.mode.toString(),
                modeSub = entryModeSubModePair.subMode,
                powerReportRx = Utils.emptyStringToNull(v.txt_power_rx?.text.toString()),
                powerReportTx = Utils.emptyStringToNull(v.txt_power_tx?.text.toString()),
                signalReportRx = Utils.emptyStringToNull(v.txt_signal_rx?.text.toString()),
                signalReportTx = Utils.emptyStringToNull(v.txt_signal_tx?.text.toString()),
                timestamp = entryTimestamp
            )
            viewModel.saveEntry(entry, entryModeSubModePair)
        }
    }

}