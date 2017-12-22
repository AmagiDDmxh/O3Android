package network.o3.o3wallet.Wallet


import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.glxn.qrgen.android.QRCode
import network.o3.o3wallet.Account
import network.o3.o3wallet.R


class MyAddressFragment : BottomSheetDialogFragment() {

    private lateinit var addressLabel: TextView
    private  lateinit var qrImageView: ImageView

    override fun setupDialog(dialog: Dialog?, style: Int) {
        val contentView = View.inflate(context, R.layout.fragment_my_address,null)
        dialog!!.setContentView(contentView)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_address, container, false)
        addressLabel = view.findViewById<TextView>(R.id.addressLabel)
        qrImageView = view.findViewById<ImageView>(R.id.addressQRCodeImageView)

        val wallet = Account.getWallet()!!
        addressLabel.text = wallet.address

        val bitmap = QRCode.from(wallet.wif).withSize(1000, 1000).bitmap()
        qrImageView.setImageBitmap(bitmap)
        return view
    }

}