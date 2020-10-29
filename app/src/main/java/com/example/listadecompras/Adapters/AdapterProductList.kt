package com.example.listadecompras.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import com.example.listadecompras.DataModels.Product
import com.example.listadecompras.R


class AdapterProductList(var contexto: Context, listado: ArrayList<Product>) : BaseAdapter() {

    var listado: ArrayList<Product>? = null

    init {
        this.listado = listado
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var precioTotal = 1
        var holder: ViewHolder? = null
        var vista: View? = convertView

        if (vista == null) {
            vista = LayoutInflater.from(contexto).inflate(R.layout.item_lst_productos, null)
            holder = ViewHolder(vista)
            vista.tag = holder
        } else {
            holder = vista.tag as? ViewHolder
        }

        val productObj = getItem(position) as Product

        precioTotal = productObj.count * productObj.price.toString().toInt()
        holder?.name?.text = productObj.name
        holder?.price?.text = "$${precioTotal}"
        holder?.count?.text = "Cantidad: ${productObj.count}"

        return vista!!
    }

    override fun getItem(position: Int): Any {
        return listado?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listado?.count()!! // para optener el valor !!
    }

    class ViewHolder(vista: View) {
        var name: TextView? = null
        var price: TextView? = null
        var count: TextView? = null

        init {
            name = vista.findViewById(R.id.item_list_productos_name)
            price = vista.findViewById(R.id.item_list_productos_price)
            count = vista.findViewById(R.id.item_list_productos_count)
        }
    }
}