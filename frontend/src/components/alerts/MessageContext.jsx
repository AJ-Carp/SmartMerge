import React, { createContext, useContext, useCallback } from 'react';

const MessageContext = createContext();

export const MessageProvider = ({ children }) => {
	// TODO: replace with a visible toast/snackbar once the UI library is decided
	const displayMessage = useCallback((message, messageType) => {
		console.log(`[${messageType}] ${message}`);
	}, []);

	return (
		<MessageContext.Provider value={{ displayMessage }}>
			{children}
		</MessageContext.Provider>
	);
};

export const useMessage = () => {
	return useContext(MessageContext);
};
