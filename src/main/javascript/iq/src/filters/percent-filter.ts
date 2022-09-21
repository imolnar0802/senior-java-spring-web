export default function(input: string) {
	const inputAsNumber = parseFloat(input);
	return (Math.round(inputAsNumber * 10000) / 100) + '%';
}
