export default  function(value: string, precision: number = 1) {
	if (!value) {
		return '';
	}
	let size = parseFloat(value);
	if (isNaN(size)) {
		return '';
	}
	if (size === 0) {
		return '0';
	}
	let suffix = 'B';
	if (size > 1024) {
		size /= 1024;
		suffix = 'KB';
	}
	if (size > 1024) {
		size /= 1024;
		suffix = 'MB';
	}
	if (size > 1024) {
		size /= 1024;
		suffix = 'GB';
	}
	const prec = Math.pow(10, precision);
	return (Math.round(size * prec) / prec) + ' ' + suffix;
}
