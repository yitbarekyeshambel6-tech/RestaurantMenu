package com.example.restaurantmenu

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Orange = Color(0xFFE85D04)
private val Cream = Color(0xFFFFF8F0)
private val Ink = Color(0xFF25211F)

data class Food(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val price: Int,
    val image: String
)

data class CartLine(val food: Food, val quantity: Int)

private val foods = listOf(
    Food(1, "Special Injera", "Ethiopian", "Injera served with traditional wot.", 180,
        "https://images.unsplash.com/photo-1547592180-85f173990554?w=900"),
    Food(2, "Beef Tibs", "Ethiopian", "Sizzling beef tibs with onion and pepper.", 350,
        "https://images.unsplash.com/photo-1544025162-d76694265947?w=900"),
    Food(3, "Cheese Pizza", "Pizza", "Pizza topped with melted cheese and fresh toppings.", 450,
        "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=900"),
    Food(4, "Classic Burger", "Burger", "Burger with lettuce, tomato and sauce.", 280,
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=900"),
    Food(5, "Fresh Salad", "Salad", "Crisp seasonal vegetables with dressing.", 150,
        "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=900"),
    Food(6, "Fresh Juice", "Drinks", "Refreshing seasonal fruit juice.", 100,
        "https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=900"),
    Food(7, "Chicken Sandwich", "Burger", "Grilled chicken sandwich with fresh greens.", 240,
        "https://images.unsplash.com/photo-1606755962773-d324e0a13086?w=900")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RestaurantApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantApp() {
    var selectedCategory by remember { mutableStateOf("All") }
    var cart by remember { mutableStateOf(mapOf<Int, Int>()) }
    var showCart by remember { mutableStateOf(false) }
    var customerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current
    val categories = listOf("All", "Ethiopian", "Pizza", "Burger", "Salad", "Drinks")
    val visibleFoods = foods.filter { selectedCategory == "All" || it.category == selectedCategory }
    val cartLines = foods.mapNotNull { food -> cart[food.id]?.takeIf { it > 0 }?.let { CartLine(food, it) } }
    val totalCount = cart.values.sum()
    val totalPrice = cartLines.sumOf { it.food.price * it.quantity }

    MaterialTheme(colorScheme = lightColorScheme(primary = Orange, onPrimary = Color.White, background = Cream, surface = Color.White, onSurface = Ink)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Orange)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("Habesha Food", fontWeight = FontWeight.Bold)
                                Text("Fresh • Tasty • Local", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    },
                    actions = {
                        BadgedBox(badge = { if (totalCount > 0) Badge { Text(totalCount.toString()) } }) {
                            IconButton(onClick = { showCart = true }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Open cart")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Cream
        ) { padding ->
            if (showCart) {
                CartScreen(
                    modifier = Modifier.padding(padding), lines = cartLines, total = totalPrice,
                    customerName = customerName, onCustomerName = { customerName = it },
                    phone = phone, onPhone = { phone = it }, address = address, onAddress = { address = it },
                    onBack = { showCart = false },
                    onChange = { id, delta ->
                        val next = (cart[id] ?: 0) + delta
                        cart = if (next <= 0) cart - id else cart + (id to next)
                    },
                    onPlaceOrder = {
                        when {
                            cartLines.isEmpty() -> Toast.makeText(context, "Add food to your cart first.", Toast.LENGTH_SHORT).show()
                            customerName.isBlank() || phone.isBlank() -> Toast.makeText(context, "Enter your name and phone number.", Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(context, "Demo order prepared. Connect a backend to send it to the restaurant.", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            } else {
                Column(Modifier.padding(padding).fillMaxSize()) {
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFE5CC)).padding(20.dp)
                    ) {
                        Text("Good food, good mood!", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                        Spacer(Modifier.height(6.dp))
                        Text("Choose your favorites and add them to your order.", color = Color.DarkGray)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { selectedCategory = "All" }, colors = ButtonDefaults.buttonColors(containerColor = Orange)) {
                            Text("Explore menu")
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { category ->
                            val active = selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (active) Orange else Color.White,
                                modifier = Modifier.clickable { selectedCategory = category }
                            ) {
                                Text(category, Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                    color = if (active) Color.White else Ink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Text("Popular menu", Modifier.padding(start = 16.dp, top = 18.dp, bottom = 8.dp), fontSize = 21.sp, fontWeight = FontWeight.Bold)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(visibleFoods, key = { it.id }) { food ->
                            FoodCard(food, cart[food.id] ?: 0,
                                onAdd = { cart = cart + (food.id to ((cart[food.id] ?: 0) + 1)) },
                                onRemove = {
                                    val next = (cart[food.id] ?: 0) - 1
                                    cart = if (next <= 0) cart - food.id else cart + (food.id to next)
                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: Food, quantity: Int, onAdd: () -> Unit, onRemove: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = food.image, contentDescription = food.name, contentScale = ContentScale.Crop,
                modifier = Modifier.size(112.dp).clip(RoundedCornerShape(14.dp)))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(food.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(food.category, color = Orange, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(food.description, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${food.price} ETB", fontWeight = FontWeight.ExtraBold, color = Orange, fontSize = 16.sp)
                    if (quantity == 0) {
                        FilledIconButton(onClick = onAdd, modifier = Modifier.size(36.dp), colors = IconButtonDefaults.filledIconButtonColors(containerColor = Orange)) {
                            Icon(Icons.Default.Add, contentDescription = "Add to cart")
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onRemove, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Remove, contentDescription = "Remove one") }
                            Text(quantity.toString(), fontWeight = FontWeight.Bold)
                            IconButton(onClick = onAdd, modifier = Modifier.size(30.dp)) { Icon(Icons.Default.Add, contentDescription = "Add one", tint = Orange) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    modifier: Modifier, lines: List<CartLine>, total: Int,
    customerName: String, onCustomerName: (String) -> Unit,
    phone: String, onPhone: (String) -> Unit,
    address: String, onAddress: (String) -> Unit,
    onBack: () -> Unit, onChange: (Int, Int) -> Unit, onPlaceOrder: () -> Unit
) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                Text("Your order", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        if (lines.isEmpty()) item { Text("Your cart is empty. Go back and add some food.") }
        items(lines, key = { it.food.id }) { line ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(line.food.name, fontWeight = FontWeight.Bold)
                        Text("${line.food.price} ETB × ${line.quantity} = ${line.food.price * line.quantity} ETB", color = Orange)
                    }
                    IconButton(onClick = { onChange(line.food.id, -1) }) { Icon(Icons.Default.Remove, contentDescription = "Remove") }
                    Text(line.quantity.toString())
                    IconButton(onClick = { onChange(line.food.id, 1) }) { Icon(Icons.Default.Add, contentDescription = "Add") }
                }
            }
        }
        item {
            HorizontalDivider()
            Text("Total: $total ETB", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Orange)
            Spacer(Modifier.height(8.dp))
            Text("Customer details", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(value = customerName, onValueChange = onCustomerName, label = { Text("Your name *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = onPhone, label = { Text("Phone number *") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = address, onValueChange = onAddress, label = { Text("Delivery address (optional)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Button(onClick = onPlaceOrder, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Orange), shape = RoundedCornerShape(12.dp)) {
                Text("Place order • $total ETB", fontWeight = FontWeight.Bold)
            }
            Text("Demo only: this order is not sent or saved. Connect a backend before accepting real orders.", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
