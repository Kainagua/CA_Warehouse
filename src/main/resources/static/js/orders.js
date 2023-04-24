let cartList=[];
function addToCart(){
let itemId=$("#itemName").val()
let item=items.find((item)=>{
return item.id==parseInt(itemId);});

let quantity=$("#itemQuantity").val()
item.quantity=quantity;
item.itemPrice=parseInt(quantity)*item.itemPrice;
cartList.push(item)
populateCart()
}

function removeItem(id){
cartList=cartList.filter((item)=>{
return item.id!=parseInt(id)
})
populateCart();
}

function populateCart(){
document.getElementById("cartBody").innerHTML="";
let counter=1;
cartList.forEach((item)=>{
let row=document.createElement("tr")
row.innerHTML=`<td>${counter}</td><td>${item.itemName}</td><td>${item.quantity}</td><td>${item.itemPrice}</td><td>
<button type="button" class="btn btn-outline-danger" id="${item.id}" onclick="removeItem(this.id)">Remove</button></td>`;
document.getElementById("cartBody").appendChild(row)
counter++;
})
}

function checkoutOrder(){
$.ajax({
url:"/makeAnOrder",
method:"POST",
dataType:"json",
contentType:"application/json; charset=utf-8",
data:JSON.stringify(cartList),
success:function(response){
if(response=="success"){
}
}
,error:function(message){
console.log(message.responseText)
}
})

}