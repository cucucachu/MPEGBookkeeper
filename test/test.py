import xlrd;
import sys;

def convert_to_text(path, outfile):
	book = xlrd.open_workbook(path)

	sheet_number = 0
	for sheet in book.sheets():
		print("Sheet Name: " + sheet.name)
		row_number = 0
		for row in sheet.get_rows():
			#print(row)
			col_number = 0
			for cell in row:
				#print("Cell in sheet " + str(sheet_number) + " at row " + str(row_number) + " and column " + str(col_number) + ": " + str(cell))
				outfile.write("Cell in sheet " + str(sheet_number) + " at row " + str(row_number) + " and column " + str(col_number) + ": " + str(cell) + "\n")

				col_number += 1
			row_number += 1
		sheet_number += 1

if len(sys.argv) != 3:
	print("python test.py excelfile1.xls excelfile2.xls")
else:
	book1 = sys.argv[1]
	book2 = sys.argv[2]

	outfile1 = open('outfile1.txt', 'w')
	outfile2 = open('outfile2.txt', 'w')

	convert_to_text(book1, outfile1)
	convert_to_text(book2, outfile2)

	outfile1.close()
	outfile2.close()

	outfile1 = open('outfile1.txt', 'r')
	outfile2 = open('outfile2.txt', 'r')

	num_diffs = 0
	line_number = 0

	for line1 in outfile1:
		line2 = outfile2.readline()
		if line1 != line2:
			print('Error in line ' + str(line_number) + ": ")
			print('   line1: ' + line1)
			print('   line2: ' + line2)
			num_diffs += 1
		line_number += 1

	if num_diffs == 0:
		print('No Diffs! :) :) :)')
	else:
		print("Total Diffs: " + str(num_diffs))



