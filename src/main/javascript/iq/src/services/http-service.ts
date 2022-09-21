import {CustomError} from '@/services/errors';

export interface IqAppErrorErrorResponse<T> {
	type: 'internal_server_error' | string;
	key?: string;
	params?: {
		[key: string]: string,
	};
	data?: T;
}

export interface HttpService {
	get(path: string): Promise<any>;

	post(path: string, body?: any): Promise<any>;

	put(path: string, body?: any): Promise<any>;

	delete(path: string, body?: any): Promise<any>;

	head(path: string): Promise<any>;
}

export class UnknownServerError extends CustomError {
	constructor() {
		super('Unknown Error');
	}
}

export class NetworkError extends CustomError {
	constructor() {
		super('Network Error');
	}
}

export class NotFoundError extends CustomError {
	constructor() {
		super('Not Found');
	}
}

export class InternalServerError extends CustomError {
	constructor() {
		super('Internal Server Error');
	}
}

export class IqAppError<T> extends CustomError implements IqAppErrorErrorResponse<T> {
	public readonly type: string;
	public readonly key?: string;
	public readonly params?: { [key: string]: string; };
	public readonly data?: T;

	constructor(serverResponse: Response, errorResponse: IqAppErrorErrorResponse<T>) {
		super(serverResponse.statusText);
		this.type = errorResponse.type;
		this.key = errorResponse.key;
		this.params = errorResponse.params;
		this.data = errorResponse.data;
	}
}

export class FetchHttpService implements HttpService {

	public async get(path: string) {
		return this.doRequest('GET', path, null);
	}

	public async post(path: string, body?: any) {
		return this.doRequest('POST', path, body);
	}

	public async put(path: string, body?: any) {
		return this.doRequest('PUT', path, body);
	}

	public async delete(path: string, body?: any) {
		return this.doRequest('DELETE', path, body);
	}

	public async head(path: string) {
		return this.doRequest('HEAD', path, null);
	}

	private async doRequest(method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'HEAD', path: string, body?: any) {
		let response;
		try {
			response = await fetch(path, this.getRequestInitData(method, body));
		} catch (e) {
			throw new NetworkError(); // Csak ha a szerver nem is érhető el (TCP error, protocol error)
		}
		return this.handleResponse(response);
	}

	private getRequestInitData(method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'HEAD', body?: any): RequestInit {
		const init: RequestInit = {
			method,
		};

		const headers = new Headers();

		headers.set('X-Requested-With', 'IqApp');

		if (body) {
			init.body = JSON.stringify(body);
			headers.set('Content-Type', 'application/json');
		}

		init.headers = headers;
		init.credentials = 'same-origin';

		return init;
	}

	private async handleResponse(response: Response): Promise<any> {
		// TODO handle all status codes
		if (response.status < 200 || response.status >= 400) {
			if (response.status === 404) {
				throw new NotFoundError();
			}

			let errorData: IqAppErrorErrorResponse<any>;

			try {
				errorData = await response.json();
			} catch (e) {
				throw new UnknownServerError();
			}

			if (errorData && errorData.type) {
				if (errorData.type === 'internal_server_error') {
					throw new InternalServerError();
				} else {
					throw new IqAppError(response, errorData);
				}
			} else {
				throw new UnknownServerError();
			}

		}

		return response.json();
	}

}
