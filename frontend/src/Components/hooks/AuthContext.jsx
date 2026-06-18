
import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/api';
const AuthContext = createContext();

export function AuthProvider({ children }) {
	const [user, setUser] = useState(() => {
		const storedUser = localStorage.getItem('userProfile');
		return storedUser ? JSON.parse(storedUser) : null;
	});

	useEffect(() => {
		if (user) {
			localStorage.setItem('userProfile', JSON.stringify(user));
		} else {
			localStorage.removeItem('userProfile');
		}
	}, [user]);

	const isLoggedIn = !!user;

	const logout = async () => {
		try {
			api.post("/auth/logout")
		} catch(e) {
			// The JWT will be cleared automatically by the backend,
			// so you don’t need to handle this unless you want to show
			// an error message.
		} finally {
			setUser(null)
			// if the error is because a invalid jwt then it will be
			// intercepted and the userProfile will be removed
			// before it gets to this if statement, thats why 
			// we need to check if it exists or not 
			if (localStorage.getItem('userProfile') !== null) {
				localStorage.removeItem('userProfile');
			}
		}
	};

	return (
		<AuthContext.Provider value={{ user, setUser, isLoggedIn, logout }}>
			{children}
		</AuthContext.Provider>
	);
}

export function useAuth() {
	return useContext(AuthContext);
}
