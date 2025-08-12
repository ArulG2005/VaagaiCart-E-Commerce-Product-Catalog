// src/actions/authActions.js
import axios from "axios";

import {
    loginFail,
    loginRequest,
    loginSuccess,
    clearError,
    registerFail,
    registerRequest,
    registerSuccess,
    loadUserRequest,
    loadUserSuccess,
    loadUserFail,
    logoutSuccess,
    logoutFail,
    updateProfileRequest,
    updateProfileSuccess,
    updateProfileFail,
    updatePasswordRequest,
    updatePasswordSuccess,
    updatePasswordFail,
    forgotPasswordRequest,
    forgotPasswordSuccess,
    forgotPasswordFail,
    resetPasswordRequest,
    resetPasswordSuccess,
    resetPasswordFail
} from "../slices/authSlice";

import {
    usersRequest,
    usersSuccess,
    usersFail,
    userRequest,
    userSuccess,
    userFail,
    deleteUserRequest,
    deleteUserSuccess,
    deleteUserFail,
    updateUserRequest,
    updateUserSuccess,
    updateUserFail
} from "../slices/userSlice";

// Helper: Handle API errors safely
const getErrorMessage = (error) => {
    return error?.response?.data?.message || error.message || "Something went wrong";
};

// LOGIN
export const login = (email, password) => async (dispatch) => {
    try {
        dispatch(loginRequest());
        const { data } = await axios.post(`http://localhost:8080/api/v1/login`, { email, password });
        console.log(data);
        dispatch(loginSuccess(data));
    } catch (error) {
        dispatch(loginFail(getErrorMessage(error)));
    }
};

// CLEAR ERRORS
export const clearAuthError = () => (dispatch) => {
    dispatch(clearError());
};

// REGISTER
export const register = (userData) => async (dispatch) => {
    try {
      
           dispatch(registerRequest());

await axios.post('http://localhost:8080/api/v1/register', userData, {
  headers: {
    'Content-Type': 'multipart/form-data',
  },
});
    dispatch(registerSuccess());
   
    } catch (error) {
        dispatch(registerFail(getErrorMessage(error)));
    }
};

// LOAD USER
export const loadUser = () => async (dispatch) => {
    try {
        dispatch(loadUserRequest());
        const { data } = await axios.get(`http://localhost:8080/api/v1/myprofile`);
        dispatch(loadUserSuccess(data));
    } catch (error) {
        dispatch(loadUserFail(getErrorMessage(error)));
    }
};

// LOGOUT
export const logout = () => async (dispatch) => {
    try {
        await axios.get(`http://localhost:8080/api/v1/logout`);
        dispatch(logoutSuccess());
    } catch (error) {
        dispatch(logoutFail(getErrorMessage(error)));
    }
};

// UPDATE PROFILE
export const updateProfile = (userData) => async (dispatch) => {
    try {
        dispatch(updateProfileRequest());
        const config = { headers: { "Content-Type": "multipart/form-data" } };
        const { data } = await axios.put(`http://localhost:8080/api/v1/update`, userData, config);
        dispatch(updateProfileSuccess(data));
    } catch (error) {
        dispatch(updateProfileFail(getErrorMessage(error)));
    }
};

// UPDATE PASSWORD
export const updatePassword = (formData) => async (dispatch) => {
    try {
        dispatch(updatePasswordRequest());
        const config = { headers: { "Content-Type": "application/json" } };
        await axios.put(`http://localhost:8080/api/v1/password/change`, formData, config);
        dispatch(updatePasswordSuccess());
    } catch (error) {
        dispatch(updatePasswordFail(getErrorMessage(error)));
    }
};

// FORGOT PASSWORD
export const forgotPassword = (formData) => async (dispatch) => {
    try {
        dispatch(forgotPasswordRequest());
        const config = { headers: { "Content-Type": "application/json" } };
        const { data } = await axios.post(`http://localhost:8080/api/v1/password/forgot`, formData, config);
        dispatch(forgotPasswordSuccess(data));
    } catch (error) {
        dispatch(forgotPasswordFail(getErrorMessage(error)));
    }
};

// RESET PASSWORD
export const resetPassword = (formData, token) => async (dispatch) => {
    try {
        dispatch(resetPasswordRequest());
        const config = { headers: { "Content-Type": "application/json" } };
        const { data } = await axios.post(`http://localhost:8080/api/v1/password/reset/${token}`, formData, config);
        dispatch(resetPasswordSuccess(data));
    } catch (error) {
        dispatch(resetPasswordFail(getErrorMessage(error)));
    }
};

export const getUsers = () => async (dispatch) => {
  try {
    dispatch(usersRequest());
    console.log('Starting request to get users');
    const { data } = await axios.get(`http://localhost:8080/api/v1/admin/users`);
    console.log("data...", data);
    dispatch(usersSuccess(data));
  } catch (error) {
    console.log('Error fetching users:', error);
    dispatch(usersFail(getErrorMessage(error)));
  }
};


// GET SINGLE USER (Admin)
export const getUser = (id) => async (dispatch) => {
    try {
        dispatch(userRequest());
        const { data } = await axios.get(`http://localhost:8080/api/v1/admin/user/${id}`);
        dispatch(userSuccess(data));
    } catch (error) {
        dispatch(userFail(getErrorMessage(error)));
    }
};

// DELETE USER (Admin)
export const deleteUser = (id) => async (dispatch) => {
    try {
        dispatch(deleteUserRequest());
        await axios.delete(`http://localhost:8080/api/v1/admin/user/${id}`);
        dispatch(deleteUserSuccess());
    } catch (error) {
        dispatch(deleteUserFail(getErrorMessage(error)));
    }
};

// UPDATE USER (Admin)
export const updateUser = (id, formData) => async (dispatch) => {
    try {
        dispatch(updateUserRequest());
        const config = { headers: { "Content-Type": "application/json" } };
        await axios.put(`http://localhost:8080/api/v1/admin/user/${id}`, formData, config);
        dispatch(updateUserSuccess());
    } catch (error) {
        dispatch(updateUserFail(getErrorMessage(error)));
    }
};
