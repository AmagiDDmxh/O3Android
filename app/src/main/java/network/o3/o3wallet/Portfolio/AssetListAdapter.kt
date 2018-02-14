package network.o3.o3wallet.Portfolio

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import network.o3.o3wallet.*
import network.o3.o3wallet.API.NEO.AccountAsset
import network.o3.o3wallet.API.O3.Portfolio
import org.jetbrains.anko.runOnUiThread
import java.text.NumberFormat

/**
 * Created by drei on 12/15/17.
 */

class AssetListAdapter(context: Context, fragment: HomeFragment): BaseAdapter() {
    data class TableCellData(var assetName: String, var assetAmount: Double,
                             var assetPrice: Double, var totalValue: Double, var percentChange: Double)

    private val mContext: Context
    private val mfragment: HomeFragment
    var assets = ArrayList<AccountAsset>()
    var portfolio: Portfolio? = null
    var referenceCurrency: CurrencyType = CurrencyType.USD
    init {
        mContext = context
        mfragment = fragment
    }

    override fun getItem(position: Int): TableCellData {
        var assetData = TableCellData("", 0.0, 0.0, 0.0, 0.0)
        assetData.assetName = assets.get(position).symbol
        assetData.assetAmount = assets.get(position).value

        if (portfolio != null) {
            if (referenceCurrency == CurrencyType.USD) {
                val latestPrice = portfolio!!.price[assets.get(position).symbol]?.averageUSD ?: 0.0
                val firstPrice = portfolio!!.firstPrice[assets.get(position).symbol]?.averageUSD ?: 0.0
                assetData.assetPrice = latestPrice
                assetData.percentChange = (latestPrice - firstPrice) / firstPrice * 100
                assetData.totalValue = latestPrice * assetData.assetAmount
            } else {
                val latestPrice = portfolio!!.price[assets.get(position).symbol]?.averageBTC ?: 0.0
                val firstPrice = portfolio!!.firstPrice[assets.get(position).symbol]?.averageBTC ?: 0.0
                assetData.assetPrice = latestPrice
                assetData.percentChange = (latestPrice - firstPrice) / firstPrice * 100
                assetData.totalValue = latestPrice * assetData.assetAmount
            }
        }

        return assetData
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return assets.count()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(R.layout.portfolio_asset_card, viewGroup, false)
        val asset = getItem(position)
        val assetNameView = view.findViewById<TextView>(R.id.assetNameTextView)
        val assetPriceView = view.findViewById<TextView>(R.id.assetPriceTextView)
        val assetAmountView = view.findViewById<TextView>(R.id.assetAmountTextView)
        val assetTotalValueView = view.findViewById<TextView>(R.id.totalValueTextView)
        val assetPercentChangeView = view.findViewById<TextView>(R.id.percentChangeTextView)

        assetNameView.text = asset.assetName
        assetPriceView.text = asset.assetPrice.formattedCurrencyString(referenceCurrency)
        assetTotalValueView.text = asset.totalValue.formattedCurrencyString(referenceCurrency)
        assetPercentChangeView.text = asset.percentChange.formattedPercentString()

        if (asset.percentChange < 0) {
            assetPercentChangeView.setTextColor(ContextCompat.getColor(mContext, R.color.colorLoss))
        } else {
            assetPercentChangeView.setTextColor(ContextCompat.getColor(mContext, R.color.colorGain))
        }

        var formatter = NumberFormat.getNumberInstance()
        formatter.maximumFractionDigits = assets[position].decimal
        assetAmountView.text = formatter.format(asset.assetAmount)



        view.setOnClickListener {
            val intent = Intent(mfragment.activity, AssetGraph::class.java)
            intent.putExtra("SYMBOL", asset.assetName.capitalize())
            mfragment.activity?.startActivity(intent)
        }

        return view
    }
}