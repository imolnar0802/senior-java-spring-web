function CustomError(this: any, message: string) {
	const instance = new Error(message);
	instance.name = this.constructor.name;
	Object.setPrototypeOf(instance, Object.getPrototypeOf(this as any));
	// FIXME captureStackTrace nincs
	// if (Error.captureStackTrace) {
	// 	Error.captureStackTrace(instance, this.constructor);
	// }
	return instance;
}

CustomError.prototype = Object.create(Error.prototype, {
	constructor: {
		value: Error,
		enumerable: false,
		writable: true,
		configurable: true,
	},
});

if (Object.setPrototypeOf) {
	Object.setPrototypeOf(CustomError, Error);
} else {
	(CustomError as any).__proto__ = Error;
}

const hacking = CustomError as any as new(message: string) => Error;

export {hacking as CustomError};
