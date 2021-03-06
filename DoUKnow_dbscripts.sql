USE [DoUKnowDB]
GO
/****** Object:  Table [dbo].[Questions]    Script Date: 08/22/2013 22:45:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Questions](
	[QNo] [bigint] IDENTITY(1,1) NOT NULL,
	[Category] [varchar](50) NOT NULL,
	[Question] [varchar](max) NOT NULL,
	[AskedBy] [varchar](25) NOT NULL,
	[SubCategory] [varchar](50) NOT NULL,
	[QDate] [date] NULL,
 CONSTRAINT [PK_Questions] PRIMARY KEY CLUSTERED 
(
	[QNo] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Answers]    Script Date: 08/22/2013 22:45:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Answers](
	[QNo] [bigint] NOT NULL,
	[Answers] [varchar](max) NOT NULL,
	[AnsweredBy] [varchar](50) NOT NULL,
	[ANo] [bigint] IDENTITY(1,1) NOT NULL,
	[ADate] [date] NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Users]    Script Date: 08/22/2013 22:45:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Users](
	[UserName] [varchar](25) NOT NULL,
	[FullName] [varchar](75) NOT NULL,
	[Password] [varchar](75) NOT NULL,
	[Profession] [varchar](25) NULL,
	[MobileNo] [varchar](15) NULL,
	[Email] [varchar](30) NULL,
	[Imei] [varchar](30) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  StoredProcedure [dbo].[spSubmitQuestion]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spSubmitQuestion] (
@PCategory as varchar(50),
@PSubCategory as varchar(50),
@PQuestion as varchar(MAX),
@PAskedBy as varchar(25) ,
@oErrorCode as int output
  
)
AS
BEGIN
BEGIN TRY
BEGIN TRANSACTION SubmitQuestion

INSERT INTO Questions(Category,Question,AskedBy,SubCategory,QDate) 
VALUES(@PCategory,@PQuestion,@PAskedBy,@PSubCategory, GETDATE())
SET @oErrorCode = 1
COMMIT TRANSACTION SubmitQuestion
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
			BEGIN
				
				ROLLBACK TRANSACTION SubmitQuestion
				SET @oErrorCode = 0
			END
		END CATCH
	END
GO
/****** Object:  StoredProcedure [dbo].[spSaveUser]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spSaveUser] (
@PUserName as varchar(25),
@PFullName as varchar(75),
@PPassword as varchar(75),
@PMobileNo as varchar(15),
@PProfession as varchar(25) ,
@PEmail as varchar(30),
@PIMEI as nvarchar(50),
@oErrorCode as int output
  
)
AS
BEGIN
BEGIN TRY
BEGIN TRANSACTION SaveUser
-- check if user alread exist
DECLARE @User VARCHAR(25) = NULL

SELECT @User = UserName FROM Users WHERE UserName = @PUserName

IF @User IS NOT NULL
SET @oErrorCode = 2 -- already exist
ELSE
-- as user doesnot exist, insert the record into table
INSERT INTO Users(UserName,FullName,Password,Profession,MobileNo,Email,Imei) 
VALUES(@PUserName,@PFullName,@PPassword,@PProfession,@PMobileNo,@PEmail,@PIMEI)
SET @oErrorCode = 1
COMMIT TRANSACTION SaveUser
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
			BEGIN
				
				ROLLBACK TRANSACTION SaveUser
				SET @oErrorCode = 0
			END
		END CATCH
	END
GO
/****** Object:  StoredProcedure [dbo].[spPostAnswer]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spPostAnswer] (
@PQNo as bigint,
@PAnswers as varchar(MAX),
@PAnsweredBy as varchar(50),
@oErrorCode as int output
  
)
AS
BEGIN
BEGIN TRY
BEGIN TRANSACTION PostAnswer

INSERT INTO Answers(QNo,Answers,AnsweredBy,ADate) 
VALUES(@PQNo,@PAnswers,@PAnsweredBy,GETDATE())
SET @oErrorCode = 1
COMMIT TRANSACTION PostAnswer
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
			BEGIN
				
				ROLLBACK TRANSACTION PostAnswer
				SET @oErrorCode = 0
			END
		END CATCH
	END
GO
/****** Object:  StoredProcedure [dbo].[spLogin]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spLogin] (
@PUserName as varchar(25),
@PPassword as varchar(75),
@oErrorCode as int output
  
)
AS
BEGIN
BEGIN TRY
BEGIN TRANSACTION LoginUser
Declare @User VARCHAR(25) = NULL

SELECT @User = UserName FROM Users WHERE UserName = @PUserName AND Password = @PPassword
IF @User IS NULL
-- Wrong credentials
SET @oErrorCode = 0
ELSE
-- Correct credentials
SET @oErrorCode = 1

COMMIT TRANSACTION LoginUser
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
			BEGIN
				
				ROLLBACK TRANSACTION LoginUser
				SET @oErrorCode = 0
			END
		END CATCH
	END
GO
/****** Object:  StoredProcedure [dbo].[spGetQuestions]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spGetQuestions] (

@PSubCategory as varchar(50)  
)
AS
BEGIN
	SELECT QNo ,Question FROM Questions WHERE SubCategory = @PSubCategory
END
GO
/****** Object:  StoredProcedure [dbo].[spGetAnswers]    Script Date: 08/22/2013 22:45:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[spGetAnswers] (

@PQNo as BIGINT  
)
AS
BEGIN
	SELECT Answers,AnsweredBy,CONVERT(VARCHAR(10),ADate,20) as ADate FROM Answers WHERE QNo = @PQNo
END
GO
