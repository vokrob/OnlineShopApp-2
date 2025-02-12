package com.vokrob.onlineshopapp_2.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.vokrob.onlineshopapp_2.Helper.ChangeNumberItemsListener
import com.vokrob.onlineshopapp_2.Helper.ManagmentCart
import com.vokrob.onlineshopapp_2.Model.ItemsModel
import com.vokrob.onlineshopapp_2.R

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CartScreen(
                managmentCart = ManagmentCart(this),
                onBackClick = { finish() }
            )
        }
    }
}

fun calculatorCart(
    managmentCart: ManagmentCart,
    tax: MutableState<Double>
) {
    val percentTax = 0.02
    tax.value = Math.round((managmentCart.getTotalFee() * percentTax) * 100) / 100.0
}

@Composable
private fun CartScreen(
    managmentCart: ManagmentCart = ManagmentCart(LocalContext.current),
    onBackClick: () -> Unit,
) {
    val cartItems = remember { mutableStateOf(managmentCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }

    calculatorCart(managmentCart, tax)

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConstraintLayout(Modifier.padding(top = 36.dp)) {
            val (backBtn, cartTxt) = createRefs()

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartTxt) { centerTo(parent) },
                text = "Your Cart",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )

            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }

        if (cartItems.value.isEmpty()) {
            Text(
                text = "Cart Is Empty",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            CartList(cartItems.value, managmentCart) {
                cartItems.value = managmentCart.getListCart()
                calculatorCart(managmentCart, tax)
            }

            CartSummary(
                itemTotal = managmentCart.getTotalFee(),
                tax = tax.value,
                delivery = 10.0
            )
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )

            Text("$$itemTotal")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Tax:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )

            Text("$$tax")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Delivery:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )

            Text("$$delivery")
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Total:",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )

            Text("$$total")
        }

        Button(
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.darkBrown)),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Check Out",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    LazyColumn(Modifier.padding(top = 16.dp)) {
        items(cartItems) { item ->
            CartItem(cartItems, item, managmentCart, onItemChange)
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    item: ItemsModel,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        val (pic, titleTxt, feeEachTime, totalEachItem, Quantity) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(item.picUrl[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .background(
                    color = colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(10.dp)
                )
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            text = item.title,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                }
                .padding(
                    start = 8.dp,
                    top = 8.dp
                )
        )

        Text(
            text = "$${item.price}",
            color = colorResource(R.color.darkBrown),
            modifier = Modifier
                .constrainAs(feeEachTime) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(titleTxt.bottom)
                }
                .padding(
                    start = 8.dp,
                    top = 8.dp
                )
        )

        Text(
            text = "$${item.numberInCart * item.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItem) {
                    start.linkTo(titleTxt.start)
                    bottom.linkTo(pic.bottom)
                }
                .padding(start = 8.dp)
        )

        ConstraintLayout(
            Modifier
                .width(100.dp)
                .constrainAs(Quantity) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    color = colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            val (plusCartBtn, minusCartBtn, numberItemText) = createRefs()

            Text(
                text = item.numberInCart.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(numberItemText) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )

            Box(
                Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        color = colorResource(R.color.darkBrown),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(plusCartBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.plusItem(
                            listItem = cartItems,
                            position = cartItems.indexOf(item),
                            listener = object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Box(
                Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(minusCartBtn) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.minusItem(
                            listItem = cartItems,
                            position = cartItems.indexOf(item),
                            listener = object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "-",
                    color = colorResource(R.color.darkBrown),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}






















