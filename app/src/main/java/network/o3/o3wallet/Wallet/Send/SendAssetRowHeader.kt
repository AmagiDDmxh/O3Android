package network.o3.o3wallet.Wallet.Send


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import network.o3.o3wallet.R


/**
 * A simple [Fragment] subclass.
 */
class SendAssetRowHeader : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wallet_send_fragment_asset_row_header, container, false)
    }

}// Required empty public constructor
