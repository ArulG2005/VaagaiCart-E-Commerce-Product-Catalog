import { createSlice } from "@reduxjs/toolkit";


const cartSlice = createSlice({
    name: 'cart',
    initialState: {
        items: localStorage.getItem('cartItems') ? JSON.parse(localStorage.getItem('cartItems')) : [],
        loading: false,
        shippingInfo: localStorage.getItem('shippingInfo') ? JSON.parse(localStorage.getItem('shippingInfo')) : {}
    },
    reducers: {
        addCartItemRequest(state) {
            state.loading = true;
        },
        addCartItemSuccess(state, action) {
            const item = action.payload;
            console.log("item...",item);
            const isItemExist = state.items.find(i => i.product === item.product);
            if (!isItemExist) {
                state.items.push(item);
            }
            state.loading = false;

            localStorage.setItem('cartItems', JSON.stringify(state.items));
        },
        increaseCartItemQty(state, action) {
         
            state.items = state.items.map(item => {
                if (item.product === action.payload) {
                    return { ...item, quantity: item.quantity + 1 };
                }
                return item;
            });
            localStorage.setItem('cartItems', JSON.stringify(state.items));
        },
        decreaseCartItemQty(state, action) {
            state.items = state.items.map(item => {
                if (item.product === action.payload) {
                    return { ...item, quantity: item.quantity - 1 };
                }
                return item;
            });
            localStorage.setItem('cartItems', JSON.stringify(state.items));
        },
        removeItemFromCart(state, action) {
            state.items = state.items.filter(item => item.product !== action.payload);
            localStorage.setItem('cartItems', JSON.stringify(state.items));
        },
        saveShippingInfo(state, action) {
            state.shippingInfo = action.payload;
            localStorage.setItem('shippingInfo', JSON.stringify(action.payload));
        },
        orderCompleted(state) {
            state.items = [];
            state.loading = false;
            state.shippingInfo = {};
            localStorage.removeItem('shippingInfo');
            localStorage.removeItem('cartItems');
            sessionStorage.removeItem('orderInfo');
        }
    }
});


const { actions, reducer } = cartSlice;

export const { 
    addCartItemRequest, 
    addCartItemSuccess,
    decreaseCartItemQty,
    increaseCartItemQty,
    removeItemFromCart,
    saveShippingInfo,
    orderCompleted
 } = actions;

export default reducer;

