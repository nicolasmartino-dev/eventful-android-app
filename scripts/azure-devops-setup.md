# Azure DevOps Setup Guide for Eventful Android App

This guide will help you set up Azure DevOps CI/CD with Tophat integration for your Android app.

## 📋 Prerequisites

- Azure DevOps account (free tier available)
- Azure Storage Account (for hosting APK files)
- GitHub account (if migrating from GitHub)
- Tophat app installed on your Mac

---

## 🚀 Step 1: Create Azure DevOps Account & Project

### 1.1 Create Azure DevOps Account

1. Go to [https://dev.azure.com](https://dev.azure.com)
2. Sign in with your Microsoft account (or create one)
3. Click **"Create organization"** or **"New organization"**
4. Choose a unique organization name (e.g., `eventful-dev`)
5. Select your region (closest to you)

### 1.2 Create a Project

1. Click **"New project"**
2. Fill in:
   - **Project name**: `eventful-android-app`
   - **Description**: "Eventful Android App CI/CD"
   - **Visibility**: Private (recommended) or Public
   - **Version control**: Git
   - **Work item process**: Basic (or Agile)
3. Click **"Create"**

---

## 🔐 Step 2: Configure Azure Storage (for APK hosting)

### 2.1 Create Storage Account

1. Go to [Azure Portal](https://portal.azure.com)
2. Click **"Create a resource"** → **"Storage account"**
3. Fill in:
   - **Subscription**: Your subscription
   - **Resource group**: Create new or use existing
   - **Storage account name**: `eventfulandroid` (must be globally unique)
   - **Region**: Same as your Azure DevOps organization
   - **Performance**: Standard
   - **Redundancy**: LRS (Locally Redundant Storage) - cheapest option
4. Click **"Review + create"** → **"Create"**

### 2.2 Create Container

1. Navigate to your storage account
2. Go to **"Containers"** in the left menu
3. Click **"+ Container"**
4. Fill in:
   - **Name**: `android-builds`
   - **Public access level**: **Private** (we'll use SAS tokens)
5. Click **"Create"**

### 2.3 Get Storage Account Key

1. In your storage account, go to **"Access keys"**
2. Copy **Key1** (you'll need this for Azure DevOps)

---

## 🔗 Step 3: Set Up Service Connections

### 3.1 Create Azure Service Connection

1. In Azure DevOps, go to **Project Settings** (gear icon)
2. Under **Pipelines**, click **"Service connections"**
3. Click **"Create service connection"**
4. Select **"Azure Resource Manager"**
5. Choose **"Service principal (automatic)"**
6. Fill in:
   - **Scope level**: Subscription
   - **Subscription**: Your Azure subscription
   - **Resource group**: The one with your storage account
   - **Service connection name**: `Azure-Service-Connection`
7. Click **"Save"**

### 3.2 Create Variable Group

1. In Azure DevOps, go to **Pipelines** → **Library**
2. Click **"+ Variable group"**
3. Name: `Eventful-Android-Secrets`
4. Add variables:
   - `GOOGLE_MAPS_API_KEY` (value: your API key, mark as secret)
   - `STORAGE_ACCOUNT_NAME` (value: your storage account name)
   - `STORAGE_ACCOUNT_KEY` (value: storage key from Step 2.3, mark as secret)
5. Click **"Save"**

---

## 📦 Step 4: Import/Migrate Code from GitHub

### Option A: Import from GitHub (Recommended)

1. In Azure DevOps, go to **Repos** → **Files**
2. Click **"Import"**
3. Select **"Git"**
4. Fill in:
   - **Clone URL**: `https://github.com/nicolasmartino-dev/eventful-android-app.git`
   - **Name**: `eventful-android-app`
5. Click **"Import"**

### Option B: Push Existing Code

```bash
cd /Users/nmartino/Documents/projects/eventful-android-app

# Add Azure DevOps remote
git remote add azure https://dev.azure.com/<your-org>/eventful-android-app/_git/eventful-android-app

# Push to Azure DevOps
git push azure main
```

---

## 🔧 Step 5: Create Pipeline

### 5.1 Create Pipeline from YAML

1. In Azure DevOps, go to **Pipelines** → **Pipelines**
2. Click **"Create Pipeline"**
3. Select **"Azure Repos Git"** (or **"GitHub"** if you prefer)
4. Select your repository: `eventful-android-app`
5. Choose **"Existing Azure Pipelines YAML file"**
6. Select branch: `main`
7. Select path: `/azure-pipelines.yml`
8. Click **"Continue"** → **"Run"**

### 5.2 Grant Permissions

If you see permission errors:
1. Click **"View"** on the permission error
2. Click **"Permit"** to grant access to the variable group

---

## 🧪 Step 6: Test the Pipeline

1. Make a small change to your code (or just trigger manually)
2. Go to **Pipelines** → **Pipelines**
3. Click on your pipeline
4. Click **"Run pipeline"**
5. Select branch: `main`
6. Click **"Run"**

### Monitor Build

1. Watch the build progress in real-time
2. Check each stage:
   - ✅ Build stage should compile the APK
   - ✅ Artifacts should be published
   - ✅ APK should be uploaded to Azure Storage
   - ✅ Tophat link should be generated

---

## 📱 Step 7: Get Tophat Link

After a successful build:

1. Go to the completed pipeline run
2. Check the **"Generate Tophat Installation Link"** step logs
3. Copy the Tophat URL from the output
4. Open Tophat app on your Mac
5. Paste the URL (or click it if it's clickable)

The app should automatically download and install!

---

## 🔄 Step 8: Enable PR Comments (Optional)

### 8.1 Create GitHub Service Connection

If you want Tophat links posted in PR comments:

1. Go to **Project Settings** → **Service connections**
2. Click **"Create service connection"**
3. Select **"GitHub"**
4. Choose **"GitHub Personal Access Token"**
5. Create a GitHub PAT with `repo` scope:
   - Go to GitHub → Settings → Developer settings → Personal access tokens
   - Generate new token with `repo` scope
6. Paste token and save as `GitHub-Connection`

### 8.2 Update Pipeline

The pipeline already includes PR comment functionality. It will automatically post Tophat links in PR comments when:
- Build is triggered by a Pull Request
- Build succeeds
- APK is uploaded successfully

---

## 🎯 Step 9: Test AI Code Review

### 9.1 Create a Pull Request

1. Create a new branch:
   ```bash
   git checkout -b feature/test-ai-review
   ```

2. Make a small change (e.g., add a comment)

3. Commit and push:
   ```bash
   git add .
   git commit -m "Test: Add comment for AI review"
   git push origin feature/test-ai-review
   ```

4. In Azure DevOps, go to **Repos** → **Pull requests**
5. Click **"New pull request"**
6. Select your branch → `main`
7. Fill in title and description
8. Click **"Create"**

### 9.2 Enable AI Code Review

1. In your PR, look for **"AI Code Review"** or **"Copilot"** section
2. Click **"Generate review"** or **"Review with AI"**
3. Wait for AI analysis (may take 1-2 minutes)
4. Review the suggestions:
   - Code quality issues
   - Security vulnerabilities
   - Best practices
   - Performance improvements

### 9.3 Review AI Suggestions

The AI will provide:
- ✅ **What's good**: Positive feedback on code quality
- ⚠️ **Suggestions**: Improvements and best practices
- 🐛 **Issues**: Potential bugs or security concerns
- 📝 **Documentation**: Missing comments or docs

---

## 🔍 Troubleshooting

### Build Fails: "local.properties not found"

**Solution**: The pipeline creates `local.properties` automatically. If it fails:
1. Check that `GOOGLE_MAPS_API_KEY` is set in variable group
2. Verify the variable group is linked to the pipeline

### Build Fails: "Android SDK not found"

**Solution**: 
1. The pipeline uses `macos-latest` which has Android SDK pre-installed
2. If issues persist, add explicit SDK setup:
   ```yaml
   - task: UseJavaVersion@0
     inputs:
       versionSpec: '11'
   ```

### Tophat Link Not Generated

**Solution**:
1. Check Azure Storage upload step succeeded
2. Verify `STORAGE_ACCOUNT_NAME` is correct
3. Check storage account key is valid
4. Ensure container `android-builds` exists

### PR Comments Not Posting

**Solution**:
1. Verify GitHub service connection is created
2. Check GitHub PAT has `repo` scope
3. Ensure PR is from the same repository

---

## 📊 Pipeline Stages Overview

### Stage 1: Build
- ✅ Checkout code
- ✅ Setup Java & Android SDK
- ✅ Create local.properties
- ✅ Build APK
- ✅ Publish artifacts
- ✅ Upload to Azure Storage
- ✅ Generate Tophat link
- ✅ Post PR comment (if PR)

### Stage 2: Lint
- ✅ Run Android Lint
- ✅ Publish lint results

---

## 🎉 Success Checklist

- [ ] Azure DevOps account created
- [ ] Project created
- [ ] Azure Storage account created
- [ ] Service connections configured
- [ ] Variable group created with secrets
- [ ] Code imported/migrated
- [ ] Pipeline created and runs successfully
- [ ] APK uploaded to Azure Storage
- [ ] Tophat link generated
- [ ] App installs via Tophat
- [ ] PR created and AI review works

---

## 📚 Additional Resources

- [Azure DevOps Documentation](https://docs.microsoft.com/azure/devops/)
- [Azure Pipelines YAML Reference](https://docs.microsoft.com/azure/devops/pipelines/yaml-schema)
- [Tophat Documentation](https://github.com/Shopify/tophat)
- [Android Build Guide](https://developer.android.com/studio/build)

---

## 💡 Tips

1. **Use variable groups** for secrets (never hardcode API keys)
2. **Enable branch policies** to require PR reviews
3. **Set up build retention** to manage artifact storage
4. **Use build badges** in your README to show build status
5. **Configure notifications** to get alerts on build failures

---

**Need Help?** Check the troubleshooting section or open an issue in your repository.


