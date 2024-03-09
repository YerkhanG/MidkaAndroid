package com.example.aviatickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aviatickets.R
import com.example.aviatickets.adapter.OfferListAdapter
import com.example.aviatickets.databinding.FragmentOfferListBinding
import com.example.aviatickets.model.entity.Offer
import com.example.aviatickets.model.entity.OfferAPI
import com.example.aviatickets.model.entity.OfferApiResponse
import com.example.aviatickets.model.network.ApiClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        val client = ApiClient.instance
        val response = client.fetchOfferList()
        response.enqueue(object : Callback<List<OfferAPI>> {
            override fun onResponse(
                call: Call<List<OfferAPI>>,
                response: Response<List<OfferAPI>>
            ) {
                println("HttpResponse: ${response.body()}")

                /**
                 * example
                 */
                val movieList = response.body()

                if (movieList != null) {
                    val offerlist = response.body()

                    if(offerlist != null){
                        val offerListMap = ArrayList(offerlist.map{
                            Offer.toOffer(it)
                        })
                        adapter.submitList(offerListMap)
                    }
                }
            }

            override fun onFailure(call: Call<List<OfferAPI>>, t: Throwable) {
                println("HttpResponse: ${t.message}")
            }
        })
    }

    private fun setupUI() {
        with(binding) {
            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sort_by_price -> {
                        val sortedList = adapter.currentList.toMutableList()
                        sortedList.sortBy { it.price }
                        adapter.submitList(sortedList)
                    }

                    R.id.sort_by_duration -> {
                        val sortedList = adapter.currentList.toMutableList()
                        sortedList.sortBy { it.flight.duration }
                        adapter.submitList(sortedList)
                    }
                }
            }
        }
    }
}