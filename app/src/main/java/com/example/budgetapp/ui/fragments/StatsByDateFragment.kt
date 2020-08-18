package com.example.budgetapp.ui.fragments

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentStatsByDateBinding
import com.example.budgetapp.viewmodels.StatsViewModel
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.android.synthetic.main.fragment_add_transaction.etDate
import kotlinx.android.synthetic.main.fragment_stats_by_date.*
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StatsByDateFragment : Fragment() {

    private lateinit var binding: FragmentStatsByDateBinding
    private val viewModel: StatsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stats_by_date, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        etDate.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    val realMonth = monthOfYear + 1
                    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.GERMAN)
                    val ld = LocalDate.parse("$dayOfMonth/$realMonth/$year", formatter)
                    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.GERMAN)
                    val output = outputFormatter.format(ld)
                    etDate.setText(output)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        viewModel.dateLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.GERMAN)
            val ld = LocalDate.parse(viewModel.dateLiveData.value.toString(), formatter)
            viewModel.getStatsByDate(ld)
            viewModel.initializePieChart(pcStatsByDate)
        })
    }
}